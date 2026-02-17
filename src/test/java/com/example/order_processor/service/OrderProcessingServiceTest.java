package com.example.order_processor.service;

import com.example.order_processor.model.Order;
import com.example.order_processor.model.OrderPlacedEvent;
import com.example.order_processor.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
//import static org.mockito.ArgumentMatchers.*;

class OrderProcessingServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessNewOrderSuccessfully() throws Exception {

        // ✅ Create event using setters (since no constructor exists)
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderId("order999");
        event.setProductId("prodA");
        event.setQuantity(2);
        event.setCustomerId("custX");
        event.setTimestamp(Instant.now().toString());

        when(orderRepository.findById("order999"))
                .thenReturn(Optional.empty());

        // Act
        orderProcessingService.processOrderPlacedEvent(event);

        // Assert
        verify(orderRepository, times(1))
                .save(any(Order.class));

        verify(rabbitTemplate, times(1))
                .convertAndSend(
                        eq("order.events"),
                        eq("order.processed"),
                        any(Object.class)
                );
    }
}
