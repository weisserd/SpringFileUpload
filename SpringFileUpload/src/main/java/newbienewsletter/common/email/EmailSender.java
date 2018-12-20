package newbienewsletter.common.email;

import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Component
public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailStatus sendM(String to, String subject, String text, Boolean isHtml) {
        LOGGER.info("EmailSender.sendM");
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            //helper.addInline("logo", new ClassPathResource("static/img/test.jpg"));
            helper.addInline("header", new ClassPathResource("static/img/header.png"));
            javaMailSender.send(mail);
            LOGGER.info("Send email '{}' to: {}", subject, to);
            return new EmailStatus(to, subject, text).success();
        } catch (Exception e) {
            LOGGER.error(String.format("Problem with sending email to: {}, error message: {}", to, e.getMessage()));
            return new EmailStatus(to, subject, text).error(e.getMessage());
        }
    }

    public String previewM(String to, String subject, String text, Boolean isHtml) {
        LOGGER.info("EmailSender.previewM");
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            MimeMessageParser parser = new MimeMessageParser(mail);
            String content = parser.parse().getHtmlContent();
            return content;
        } catch (Exception e) {
            LOGGER.error(String.format("Problem with previewing email", e.getMessage()));
            return e.getMessage();
        }
    }

    public String getNewsletterDate() throws ParseException {
        LOGGER.info("EmailSender.getNewsletterDate");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM yyyy", Locale.GERMAN);
        Calendar actualDate = Calendar.getInstance();
        LOGGER.info("Monat = " + actualDate.getTime());
        String month_name = month_date.format(actualDate.getTime());
        return month_name;
    }

    public String getNewsletterDateMinusOneMonth() {
        LOGGER.info("EmailSender.getNewsletterDateMinusOneMonth()");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM yyyy", Locale.GERMAN);
        Calendar actualDate = Calendar.getInstance();
        actualDate.add(Calendar.MONTH, -1);
        LOGGER.info("Monat vorher = " + actualDate.getTime());
        String monthBefore = month_date.format(actualDate.getTime());
        return monthBefore;
    }

    public void sendMail(Email email){
        LOGGER.info(email.content);
        sendM("vincent.welker@exxeta.com", "test", email.content , true );

    }
}