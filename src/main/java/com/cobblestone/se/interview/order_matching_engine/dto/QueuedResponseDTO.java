package com.cobblestone.se.interview.order_matching_engine.dto;

public class QueuedResponseDTO {
        public String clientOrderId;
        public String status;
        public String message;

        public QueuedResponseDTO(String clientOrderId, String status, String message) {
            this.clientOrderId = clientOrderId;
            this.status = status;
            this.message = message;
        }
}

