package com.github.dlism.backend.services.Impl;

import com.github.dlism.backend.services.ProduceService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ProduceServiceImpl implements ProduceService {
    private final RabbitTemplate rabbitTemplate;

    public ProduceServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceAnswer(String email) {
        rabbitTemplate.convertAndSend("testTopicName", "test_routing_key", "ssss");
    }
}
