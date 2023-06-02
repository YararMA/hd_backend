package com.github.dlism.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Value("${rabbitmq.queue}")
    private String queue;

    @Value("${rabbitmq.topic}")
    private String topic;

    @Value("${rabbitmq.routing_key}")
    private String routing_key;
    @Bean
    public MessageConverter jsonMessageConvertor(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(topic);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routing_key);
    }
}
