package com.example.mailsender.service.Impl;

import com.example.mailsender.service.ConsumerService;
import com.example.mailsender.service.EmailSenderService;
import com.github.dlism.model.RabbitmqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Autowired
    private EmailSenderService emailSenderService;
    @RabbitListener(queues = "${rabbitmq.queue_json}")
    @Override
    public void consume(RabbitmqDto message) {
        LOGGER.info(String.format("Received message ->%s", message));
        emailSenderService.sendEmail(message.getEmail(), message.getCode().toString());
    }
}
