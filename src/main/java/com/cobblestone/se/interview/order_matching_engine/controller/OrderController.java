package com.cobblestone.se.interview.order_matching_engine.controller;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.dto.OrderResponseDTO;
import com.cobblestone.se.interview.order_matching_engine.dto.QueuedResponseDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.service.OrderMatchingService;
import com.cobblestone.se.interview.order_matching_engine.service.kafka.OrderKafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<QueuedResponseDTO> placeOrder(@RequestBody OrderRequestDTO dto) {
        if (dto.clientOrderId == null) {
            dto.clientOrderId = UUID.randomUUID().toString();
        }
        kafkaProducer.sendOrder(dto);
        return ResponseEntity.ok(new QueuedResponseDTO(dto.clientOrderId,"PENDING","Order submitted"));
    }

    @GetMapping("/client/{clientOrderId}")
    public ResponseEntity<OrderResponseDTO> getByClientOrderId(@PathVariable String clientOrderId) {
        Optional<Order> optionalOrder = orderService.findOptionalByClientOrderId(clientOrderId);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.ok(new OrderResponseDTO(null, "PENDING", "Order not received yet"));
        }

        Order order = optionalOrder.get();
        return ResponseEntity.ok(new OrderResponseDTO(order.getId(), order.getStatus(), order.getSymbol()));
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
