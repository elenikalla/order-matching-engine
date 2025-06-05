package com.cobblestone.se.interview.order_matching_engine.service.kafka;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.service.OrderMatchingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {

    private final OrderMatchingService matchingService;

    public OrderKafkaConsumer(OrderMatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @KafkaListener(topics = "orders", groupId = "order-matcher", containerFactory = "orderKafkaListenerContainerFactory")
    public void consume(OrderRequestDTO dto) {
        matchingService.handle(dto);
    }
}