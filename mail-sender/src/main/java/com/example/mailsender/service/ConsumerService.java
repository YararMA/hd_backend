package com.example.mailsender.service;

import com.github.dlism.model.RabbitmqDto;

public interface ConsumerService {
    void consume(RabbitmqDto message);
}
