package com.example.order_processor.model;

import lombok.Data;

@Data
public class OrderProcessedEvent {
    private String orderId;
    private String status;
    private String processedAt;
}
