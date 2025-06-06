package com.cobblestone.se.interview.order_matching_engine.controller;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.dto.OrderResponseDTO;
import com.cobblestone.se.interview.order_matching_engine.dto.QueuedResponseDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import com.cobblestone.se.interview.order_matching_engine.service.OrderMatchingService;
import com.cobblestone.se.interview.order_matching_engine.kafka.OrderKafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderKafkaProducer kafkaProducer;
    private final OrderMatchingService orderService;
    private final TradeRepository tradeRepository;

    public OrderController(OrderKafkaProducer kafkaProducer,OrderMatchingService orderService, TradeRepository tradeRepository) {
        this.kafkaProducer = kafkaProducer;
        this.orderService = orderService;
        this.tradeRepository = tradeRepository;
    }

    @PostMapping
    public ResponseEntity<QueuedResponseDTO> placeOrder(@RequestBody OrderRequestDTO dto) {
        if (dto.clientOrderId == null) {
            dto.clientOrderId = UUID.randomUUID().toString();
        }
        kafkaProducer.sendOrder(dto);
        return ResponseEntity.ok(new QueuedResponseDTO(dto.clientOrderId,OrderStatus.PENDING,"Order submitted"));
    }

    @GetMapping("/client/{clientOrderId}")
    public ResponseEntity<OrderResponseDTO> getByClientOrderId(@PathVariable String clientOrderId) {
        Optional<Order> optionalOrder = orderService.findOptionalByClientOrderId(clientOrderId);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.ok(new OrderResponseDTO(null, OrderStatus.PENDING, "Order not received yet",null));
        }

        Order order = optionalOrder.get();
        return ResponseEntity.ok(new OrderResponseDTO(order.getId(), order.getStatus(), order.getSymbol(),order.getCreatedAt()));
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
