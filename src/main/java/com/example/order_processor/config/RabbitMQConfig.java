package com.example.order_processor.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "order.events";
    public static final String DLX = "dlx.order.events";
    public static final String QUEUE = "order.placed.queue";
    public static final String DLQ = "order.dlq";

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX, true, false);
    }

    @Bean
    public Queue orderPlacedQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", "order.placed");
        return new Queue(QUEUE, true, false, false, args);
    }

    @Bean
    public Queue orderDlq() {
        return new Queue(DLQ, true);
    }

    @Bean
    public Binding bindOrderPlacedQueue() {
        return BindingBuilder
                .bind(orderPlacedQueue())
                .to(orderEventsExchange())
                .with("order.placed");
    }

    @Bean
    public Binding bindOrderDlq() {
        return BindingBuilder
                .bind(orderDlq())
                .to(deadLetterExchange())
                .with("order.placed");
    }
}
