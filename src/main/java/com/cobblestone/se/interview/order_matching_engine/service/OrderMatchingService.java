package com.cobblestone.se.interview.order_matching_engine.service;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderMatchingService {

    private final OrderRepository repository;

    public OrderMatchingService(OrderRepository repository) {
        this.repository = repository;
    }
    public Order handle(OrderRequestDTO dto) {
        Order order = new Order();
        order.setSymbol(dto.symbol);
        order.setPrice(dto.price);
        order.setQuantity(dto.quantity);
        order.setType(dto.type);
        order.setStatus("PENDING");

        order.setClientOrderId(dto.clientOrderId);
        return addOrder(order);
    }

    public Order addOrder(Order order) {
        order.setStatus("PENDING");
        Order savedOrder = repository.save(order);
        if (order.getType().equalsIgnoreCase("BUY")) {
            matchBuyOrder(savedOrder);
        } else {
            matchSellOrder(savedOrder);
        }
        return repository.save(savedOrder);
    }

    private void matchBuyOrder(Order buyOrder) {
        List<Order> sells = repository.findBySymbolIgnoreCase(buyOrder.getSymbol()).stream()
                .filter(o -> o.getType().equals("SELL"))
                .sorted(Comparator.comparingDouble(Order::getPrice))
                .toList();

        for (Order sell : sells) {
            if (buyOrder.getQuantity() == 0) break;
            if (sell.getPrice() <= buyOrder.getPrice()) {
                int tradedQty = Math.min(buyOrder.getQuantity(), sell.getQuantity());
                buyOrder.setQuantity(buyOrder.getQuantity() - tradedQty);
                sell.setQuantity(sell.getQuantity() - tradedQty);
                System.out.printf("Trade BUY %s: %d @ %.2f\n", buyOrder.getSymbol(), tradedQty, sell.getPrice());
                if (sell.getQuantity() == 0) repository.delete(sell);
                else repository.save(sell);
            }
        }

        if (buyOrder.getQuantity() > 0) repository.save(buyOrder);
        else repository.delete(buyOrder);
    }

    private void matchSellOrder(Order sellOrder) {
        List<Order> buys = repository.findBySymbolIgnoreCase(sellOrder.getSymbol()).stream()
                .filter(o -> o.getType().equals("BUY"))
                .sorted((a, b) -> Double.compare(b.getPrice(), a.getPrice()))
                .toList();

        for (Order buy : buys) {
            if (sellOrder.getQuantity() == 0) break;
            if (buy.getPrice() >= sellOrder.getPrice()) {
                int tradedQty = Math.min(sellOrder.getQuantity(), buy.getQuantity());
                sellOrder.setQuantity(sellOrder.getQuantity() - tradedQty);
                buy.setQuantity(buy.getQuantity() - tradedQty);
                System.out.printf("Trade SELL %s: %d @ %.2f\n", sellOrder.getSymbol(), tradedQty, buy.getPrice());
                if (buy.getQuantity() == 0) repository.delete(buy);
                else repository.save(buy);
            }
        }

        if (sellOrder.getQuantity() > 0) repository.save(sellOrder);
        else repository.delete(sellOrder);
    }

    public Optional<Order> findOptionalByClientOrderId(String clientOrderId) {
        return repository.findByClientOrderId(clientOrderId);
    }
}
