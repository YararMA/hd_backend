package com.example.mailsender.service.Impl;

import com.example.mailsender.model.EmailTemplate;
import com.example.mailsender.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.activation_url}")
    private String url;
    @Override
    public void sendEmail(String toEmail, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(toEmail);
        message.setSubject("Исторический диктант");
        message.setText(EmailTemplate.message(url, code));

        javaMailSender.send(message);

    }
}
