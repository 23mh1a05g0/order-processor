package com.example.order_processor.service;

import com.example.order_processor.model.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderProcessingService service;

    @RabbitListener(queues = "order.placed.queue")
    public void handleOrderPlaced(OrderPlacedEvent event) {

        log.info("Received OrderPlacedEvent orderId={}", event.getOrderId());

        service.processOrderPlacedEvent(event);
    }
}
