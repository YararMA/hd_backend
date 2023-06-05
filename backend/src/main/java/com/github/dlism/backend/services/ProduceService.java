package com.github.dlism.backend.services;

import com.github.dlism.model.RabbitmqDto;

public interface ProduceService {
    void produceAnswer(RabbitmqDto rabbitmqDto);
}
