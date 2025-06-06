package com.cobblestone.se.interview.order_matching_engine.dto;

import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import java.time.LocalDateTime;

public class OrderResponseDTO {
    public Long orderId;
    public OrderStatus status;
    public String message;
    public LocalDateTime createdAt;

    public OrderResponseDTO(Long orderId, OrderStatus status, String message, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }
}