package com.example.order_processor.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {

    private String orderId;
    private String productId;
    private int quantity;
    private String customerId;
    private String timestamp;
}
