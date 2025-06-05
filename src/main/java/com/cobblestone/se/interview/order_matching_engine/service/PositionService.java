package com.cobblestone.se.interview.order_matching_engine.service;


import com.cobblestone.se.interview.order_matching_engine.dto.PositionDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final OrderRepository orderRepository;

    public PositionService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<PositionDTO> calculatePositions() {
        List<Order> allOrders = orderRepository.findAll();

        Map<String, PositionDTO> map = new HashMap<>();

        for (Order order : allOrders) {
            String symbol = order.getSymbol();
            PositionDTO pos = map.getOrDefault(symbol, new PositionDTO(symbol));

            if (order.getType().equalsIgnoreCase("BUY")) {
                pos.buyQty += order.getQuantity();
                pos.buyTotal += order.getQuantity() * order.getPrice();
            } else if (order.getType().equalsIgnoreCase("SELL")) {
                pos.sellQty += order.getQuantity();
                pos.sellTotal += order.getQuantity() * order.getPrice();
            }
            map.put(symbol, pos);
        }

        return map.values().stream().peek(PositionDTO::finalizeMetrics).collect(Collectors.toList());
    }
}
