package com.one.frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String to, String verificationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your account");
        message.setText("Please click the following link to verify your account: " + verificationUrl);
        mailSender.send(message);
    }
}
