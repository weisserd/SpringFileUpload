package newbienewsletter.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailHtmlSender {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private TemplateEngine templateEngine;

    private boolean isHtml = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    public EmailStatus send(String to, String subject, String templateName, Context context) {
        LOGGER.info("EmailHtmlSender.send");
        String body = templateEngine.process(templateName, context);
        LOGGER.info(body);
        return emailSender.sendM(to, subject, body, isHtml);
    }

    public String preview(String to, String subject, String templateName, Context context) {
        LOGGER.info("EmailHtmlSender.preview");
        String body = templateEngine.process(templateName, context);
        return emailSender.previewM(to, subject, body, isHtml);
    }
}