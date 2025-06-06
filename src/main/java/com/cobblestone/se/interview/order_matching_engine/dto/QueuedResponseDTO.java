package com.cobblestone.se.interview.order_matching_engine.dto;

import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;

public class QueuedResponseDTO {
        public String clientOrderId;
        public OrderStatus status;
        public String message;

        public QueuedResponseDTO(String clientOrderId, OrderStatus status, String message) {
            this.clientOrderId = clientOrderId;
            this.status = status;
            this.message = message;
        }
}

