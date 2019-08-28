package com.ruczajsoftware.workoutrival.service;

import com.ruczajsoftware.workoutrival.config.EmailConfiguration;
import com.ruczajsoftware.workoutrival.model.email.EmailTemplate;
import com.ruczajsoftware.workoutrival.model.email.EmailTemplateBuilder;
import com.ruczajsoftware.workoutrival.model.email.EmailTemplates;
import com.ruczajsoftware.workoutrival.service.util.Bundle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

    private final EmailConfiguration configuration;

    public void sendPasswordResetPinToUser(String username, String userEmail, String pin) {
        final EmailTemplate emailTemplate = new EmailTemplateBuilder()
                .email(userEmail)
                .subject("Password Reset PIN")
                .payload(Bundle.getEmailTemplate(Locale.ENGLISH, EmailTemplates.RESET_PASSWORD.getKey(), username, pin))
                .build();
        try {
            sendTextEmail(emailTemplate);
        } catch (MessagingException e) {
            log.error("Sending email error: " + e);
        }
    }

    private void sendTextEmail(EmailTemplate emailTemplate) throws MessagingException {
        Session session = configuration.getSession();

        Transport transport = session.getTransport();
        InternetAddress addressFrom = new InternetAddress(configuration.getUsername());

        MimeMessage message = new MimeMessage(session);
        message.setSender(addressFrom);
        message.setSubject(emailTemplate.getSubject());
        message.setContent(emailTemplate.getPayload(), "text/plain");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTemplate.getEmail()));

        transport.connect();
        Transport.send(message);
        transport.close();
    }
}
