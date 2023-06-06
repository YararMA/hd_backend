package com.example.mailsender.service;

public interface EmailSenderService {
    void sendEmail(String toEmail, String code);
}
