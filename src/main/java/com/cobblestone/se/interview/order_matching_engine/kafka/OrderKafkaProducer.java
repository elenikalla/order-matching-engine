package com.cobblestone.se.interview.order_matching_engine.kafka;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderRequestDTO> kafkaTemplate;

    public OrderKafkaProducer(KafkaTemplate<String, OrderRequestDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(OrderRequestDTO dto) {
        String topic = "order-topic";
        String key = dto.symbol;
        kafkaTemplate.send(topic, key, dto);
    }
}