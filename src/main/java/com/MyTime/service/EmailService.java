package com.MyTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String recipientEmail, String subject, String body, byte[] attachmentData, String attachmentName) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(body);

        // Adjuntar el archivo CSV
        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentData));

        javaMailSender.send(mimeMessage);
    }

    public void sendEmailEvent(String recipientEmail, String subject, String body) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        //helper.setContent(body);
        mimeMessage.setContent(body,"text/html");

        javaMailSender.send(mimeMessage);
    }

}
