package com.cobblestone.se.interview.order_matching_engine.controller;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.dto.QueuedResponseDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.service.OrderMatchingService;
import com.cobblestone.se.interview.order_matching_engine.kafka.OrderKafkaProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderKafkaProducer kafkaProducer;
    private final OrderMatchingService orderService;

    public OrderController(OrderKafkaProducer kafkaProducer,OrderMatchingService orderService) {
        this.kafkaProducer = kafkaProducer;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<QueuedResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO dto) {
        if (dto.clientOrderId == null) {
            dto.clientOrderId = UUID.randomUUID().toString();
        }
        kafkaProducer.sendOrder(dto);
        return ResponseEntity.ok(new QueuedResponseDTO(dto.clientOrderId,OrderStatus.PENDING,"Order submitted"));
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
