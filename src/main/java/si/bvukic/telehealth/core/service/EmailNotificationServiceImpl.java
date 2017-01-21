/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import si.bvukic.telehealth.core.model.User;

/**
 *
 * @author vukicb
 */
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;

    @Autowired(required = true)
    @Qualifier("velocityEngine")
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired(required = true)
    @Qualifier("velocityEngine")
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public void sendRegistrationNotification(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(user.getEmail());
                message.setFrom(new InternetAddress("bostjan.vukic@gmail.com"));
                message.setSubject("Dobrodošli");
                message.setSentDate(new Date());
                Map model = new HashMap();
                model.put("user", user);

                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, "mail/registerdMessage.vm", "UTF-8", model);
                message.setText(text, true);
            }
        };
        mailSender.send(preparator);
    }

    @Override
    public void sendPasswordResetNotification(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
