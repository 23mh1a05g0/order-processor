package com.example.order_processor.service;

import com.example.order_processor.model.*;
import com.example.order_processor.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void processOrderPlacedEvent(OrderPlacedEvent event) {

        log.info("Processing orderId={}", event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Idempotency check
        if (order.getStatus() == OrderStatus.PROCESSED) {
            log.info("Order already processed: {}", order.getId());
            return;
        }

        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        // Simulate business logic
        order.setStatus(OrderStatus.PROCESSED);
        orderRepository.save(order);

        publishOrderProcessedEvent(order.getId());
    }

    public void publishOrderProcessedEvent(String orderId) {

        OrderProcessedEvent event = new OrderProcessedEvent();
        event.setOrderId(orderId);
        event.setStatus("PROCESSED");
        event.setProcessedAt(Instant.now().toString());

        rabbitTemplate.convertAndSend(
                "order.events",
                "order.processed",
                event
        );

        log.info("Published OrderProcessedEvent for orderId={}", orderId);
    }
}
