package com.cobblestone.se.interview.order_matching_engine.dto;

public class OrderResponseDTO {
    public Long orderId;
    public String status;
    public String message;

    public OrderResponseDTO(Long orderId, String status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }
}