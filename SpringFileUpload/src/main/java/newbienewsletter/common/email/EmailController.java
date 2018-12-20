package newbienewsletter.common.email;

import newbienewsletter.document.DocumentService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class EmailController {

    @Autowired
    EmailHtmlSender emailHtmlSender;
    @Autowired
    EmailSender emailSender;
    @Autowired
    DocumentService documentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @RequestMapping(value = "api/mail/sendwithoutpreview", method = POST)
    public ResponseEntity<String> sendEmailWithTemplate() throws IOException, InvalidFormatException, MessagingException, ParseException {
        LOGGER.info("FileUploadController.sendEmailWithTemplate");
        Context context = new Context();
        context.setVariable("description", "just a description variable");
        context.setVariable("logo", "logo");
        context.setVariable("header", "header");
        context.setVariable("date", emailSender.getNewsletterDate());
        context.setVariable("datebefore", emailSender.getNewsletterDateMinusOneMonth());
        context.setVariable("documentList", documentService.getAllDocuments());
        LOGGER.info("send mail");
        emailHtmlSender.send("vincent.welker@exxeta.com", "Newbies und Austritte im " + emailSender.getNewsletterDate(), "email/templateMail", context);
        LOGGER.info("okay");
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }

    @RequestMapping(value = "api/preview", method = GET)
    public ResponseEntity<String> showPreviewMailWithTemplate() throws IOException, InvalidFormatException, MessagingException, ParseException {
        LOGGER.info("EmailController.showPreviewMailWithTemplate");
        Context context = new Context();
        context.setVariable("description", "just a description variable");
        context.setVariable("logo", "logo");
        context.setVariable("header", "header");
        context.setVariable("date", emailSender.getNewsletterDate());
        context.setVariable("datebefore", emailSender.getNewsletterDateMinusOneMonth());
        context.setVariable("documentList", documentService.getAllDocuments());
        return new ResponseEntity<>(emailHtmlSender.preview("vincent.welker@exxeta.com", "Newbies und Austritte im " + emailSender.getNewsletterDate(), "email/templatePreview", context), HttpStatus.OK);
    }

    @RequestMapping(value = "api/sendmail", method = POST)
    @ResponseBody
    public ResponseEntity<String> getJsonFromFrontend(@Valid @RequestBody final Email email) throws IOException, ParseException {
        Context context = new Context();
        context.setVariable("header", "header");
        context.setVariable("date", emailSender.getNewsletterDate());
        context.setVariable("datebefore", emailSender.getNewsletterDateMinusOneMonth());
        context.setVariable("body", email.content);
        this.emailHtmlSender.send("vincent.welker@exxeta.com", "Newbies und Austritte im " + emailSender.getNewsletterDate(), "email/templateMail", context);
        return new ResponseEntity<> ("its done",HttpStatus.OK);
    }
}
