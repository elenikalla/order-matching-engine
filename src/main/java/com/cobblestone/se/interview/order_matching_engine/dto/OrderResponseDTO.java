package com.cobblestone.se.interview.order_matching_engine.dto;

import java.time.LocalDateTime;

public class OrderResponseDTO {
    public Long orderId;
    public String status;
    public String message;
    public LocalDateTime createdAt;

    public OrderResponseDTO(Long orderId, String status, String message,LocalDateTime createdAt) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }
}