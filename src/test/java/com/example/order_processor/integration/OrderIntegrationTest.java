package com.example.order_processor.integration;

import com.example.order_processor.model.Order;
import com.example.order_processor.model.OrderPlacedEvent;
import com.example.order_processor.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")   // ✅ Use H2 test configuration
class OrderIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldConsumeEventAndUpdateDatabase() throws InterruptedException {

        // Arrange
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderId("integration123");
        event.setProductId("prodX");
        event.setQuantity(1);
        event.setCustomerId("custY");
        event.setTimestamp(Instant.now().toString());

        // Act
        rabbitTemplate.convertAndSend(
                "order.events",
                "order.placed",
                event
        );

        // Wait for async RabbitMQ listener to process
        Thread.sleep(3000);

        // Assert
        Optional<Order> optionalOrder =
                orderRepository.findById("integration123");

        assertThat(optionalOrder).isPresent();

        Order order = optionalOrder.get();

        assertThat(order.getStatus().name())
                .isEqualTo("PROCESSED");
    }
}
