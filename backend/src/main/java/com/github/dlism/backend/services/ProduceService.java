package com.github.dlism.backend.services;


import com.github.dlism.backend.dto.RabbitmqDto;

public interface ProduceService {
    void produceAnswer(RabbitmqDto rabbitmqDto);
}
