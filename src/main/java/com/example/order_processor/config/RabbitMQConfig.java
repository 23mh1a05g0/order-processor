package com.example.order_processor.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String DEAD_LETTER_EXCHANGE = "order.dlx";

    public static final String ORDER_PLACED_QUEUE = "order.placed.queue";
    public static final String ORDER_PROCESSED_QUEUE = "order.processed.queue";
    public static final String ORDER_DLQ = "order.dlq";

    public static final String ORDER_PLACED_KEY = "order.placed";
    public static final String ORDER_PROCESSED_KEY = "order.processed";
    public static final String ORDER_DLQ_KEY = "order.dlq";


    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }


    @Bean
    public Queue orderPlacedQueue() {
        return QueueBuilder
                .durable(ORDER_PLACED_QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(ORDER_DLQ_KEY)
                .build();
    }

    @Bean
    public Queue orderProcessedQueue() {
        return QueueBuilder
                .durable(ORDER_PROCESSED_QUEUE)
                .build();
    }

    @Bean
    public Queue orderDlq() {
        return QueueBuilder
                .durable(ORDER_DLQ)
                .build();
    }


    @Bean
    public Binding bindOrderPlacedQueue() {
        return BindingBuilder
                .bind(orderPlacedQueue())
                .to(orderEventsExchange())
                .with(ORDER_PLACED_KEY);
    }

    @Bean
    public Binding bindOrderProcessedQueue() {
        return BindingBuilder
                .bind(orderProcessedQueue())
                .to(orderEventsExchange())
                .with(ORDER_PROCESSED_KEY);
    }

    @Bean
    public Binding bindOrderDlq() {
        return BindingBuilder
                .bind(orderDlq())
                .to(deadLetterExchange())
                .with(ORDER_DLQ_KEY);
    }
}
