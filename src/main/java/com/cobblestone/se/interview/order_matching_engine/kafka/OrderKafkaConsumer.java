package com.cobblestone.se.interview.order_matching_engine.kafka;

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

    @KafkaListener(topics = "order-topic", groupId = "order-matching")
    public void consume(OrderRequestDTO dto) {
        System.out.println("Consumed Order: " + dto);
        matchingService.handle(dto);
    }
}