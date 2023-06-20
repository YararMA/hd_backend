package com.example.mailsender.service;


import com.example.mailsender.dto.RabbitmqDto;

public interface ConsumerService {
    void consume(RabbitmqDto message);
}
