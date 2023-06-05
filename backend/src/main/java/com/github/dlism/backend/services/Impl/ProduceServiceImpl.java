package com.github.dlism.backend.services.Impl;

import com.github.dlism.backend.services.ProduceService;
import com.github.dlism.model.RabbitmqDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProduceServiceImpl implements ProduceService {

    @Value("${rabbitmq.topic}")
    private String topic;

    @Value("${rabbitmq.routing_key}")
    private String routing_key;

    private final RabbitTemplate rabbitTemplate;

    public ProduceServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceAnswer(String email) {
        RabbitmqDto rabbitmqDto = new RabbitmqDto();
        rabbitmqDto.setEmail(email);
        rabbitTemplate.convertAndSend(topic, routing_key, rabbitmqDto);
    }
}
