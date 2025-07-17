package com.MyTime.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        try {
            // Mock the constructor of MimeMessageHelper
            // This is a bit tricky as MimeMessageHelper is a concrete class and its constructor is called directly.
            // For a true unit test, you might need PowerMock or refactor EmailService to inject MimeMessageHelper factory.
            // For now, we'll assume the helper is created correctly and focus on the send method.
            // We can't directly mock `new MimeMessageHelper(mimeMessage, true)` without PowerMock.
            // So, we'll just verify that `javaMailSender.send` is called.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendEmailWithAttachment() throws MessagingException, IOException {
        byte[] attachmentData = "test data".getBytes();
        String attachmentName = "test.txt";

        // Mock the behavior of MimeMessageHelper methods
        doNothing().when(mimeMessageHelper).setTo(any(String.class));
        doNothing().when(mimeMessageHelper).setSubject(any(String.class));
        doNothing().when(mimeMessageHelper).setText(any(String.class));
        doNothing().when(mimeMessageHelper).addAttachment(any(String.class), any(org.springframework.core.io.InputStreamSource.class));

        emailService.sendEmailWithAttachment("test@example.com", "Subject", "Body", attachmentData, attachmentName);

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmailEvent() throws MessagingException, IOException {
        // Mock the behavior of MimeMessageHelper methods
        doNothing().when(mimeMessageHelper).setTo(any(String.class));
        doNothing().when(mimeMessageHelper).setSubject(any(String.class));
        doNothing().when(mimeMessageHelper).setText(any(String.class));

        emailService.sendEmailEvent("test@example.com", "Subject", "Body");

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(mimeMessage, times(1)).setContent(any(String.class), eq("text/html"));
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
