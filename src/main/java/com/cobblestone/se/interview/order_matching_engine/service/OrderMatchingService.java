package com.cobblestone.se.interview.order_matching_engine.service;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.Trade;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderMatchingService {

    private final OrderRepository orderRepository;

    private final TradeRepository tradeRepository;

    public OrderMatchingService(OrderRepository orderRepository, TradeRepository tradeRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
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
        Order savedOrder = orderRepository.save(order);
        if (order.getType().equalsIgnoreCase("BUY")) {
            matchBuyOrder(savedOrder);
        } else {
            matchSellOrder(savedOrder);
        }
        return orderRepository.save(savedOrder);
    }

    private void matchBuyOrder(Order buyOrder) {
        boolean wasMatched = false;

        List<Order> sells = orderRepository.findBySymbolIgnoreCase(buyOrder.getSymbol()).stream()
                .filter(o -> o.getType().equals("SELL"))
                .sorted(Comparator.comparingDouble(Order::getPrice).thenComparing(Order::getCreatedAt))
                .toList();

        for (Order sell : sells) {
            if (buyOrder.getQuantity() == 0) break;
            if (sell.getPrice() <= buyOrder.getPrice()) {
                int tradedQty = Math.min(buyOrder.getQuantity(), sell.getQuantity());

                buyOrder.setQuantity(buyOrder.getQuantity() - tradedQty);
                sell.setQuantity(sell.getQuantity() - tradedQty);
                wasMatched = true;

                recordTrade(buyOrder.getSymbol(), buyOrder.getId().toString(), sell.getId().toString(), sell.getPrice(), tradedQty);

                sell.setStatus(sell.getQuantity() == 0 ? "FILLED" : "PARTIALLY_FILLED");
                orderRepository.save(sell);
            }
        }

        if (!wasMatched) {
            buyOrder.setStatus("PENDING");
        } else if (buyOrder.getQuantity() > 0) {
            buyOrder.setStatus("PARTIALLY_FILLED");
        } else {
            buyOrder.setStatus("FILLED");
        }

        orderRepository.save(buyOrder);
    }

    private void matchSellOrder(Order sellOrder) {
        boolean wasMatched = false;

        List<Order> buys = orderRepository.findBySymbolIgnoreCase(sellOrder.getSymbol()).stream()
                .filter(o -> o.getType().equals("BUY"))
                .sorted(Comparator.comparingDouble(Order::getPrice).reversed().thenComparing(Order::getCreatedAt))
                .toList();

        for (Order buy : buys) {
            if (sellOrder.getQuantity() == 0) break;
            if (buy.getPrice() >= sellOrder.getPrice()) {
                int tradedQty = Math.min(sellOrder.getQuantity(), buy.getQuantity());

                sellOrder.setQuantity(sellOrder.getQuantity() - tradedQty);
                buy.setQuantity(buy.getQuantity() - tradedQty);
                wasMatched = true;

                recordTrade(sellOrder.getSymbol(), buy.getId().toString(), sellOrder.getId().toString(), buy.getPrice(), tradedQty);

                buy.setStatus(buy.getQuantity() == 0 ? "FILLED" : "PARTIALLY_FILLED");
                orderRepository.save(buy);
            }
        }

        if (!wasMatched) sellOrder.setStatus("PENDING");
        else if (sellOrder.getQuantity() > 0) sellOrder.setStatus("PARTIALLY_FILLED");
        else sellOrder.setStatus("FILLED");

        orderRepository.save(sellOrder);
    }
    private void recordTrade(String symbol, String buyOrderId, String sellOrderId, double price, int quantity) {
        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setBuyOrderId(buyOrderId);
        trade.setSellOrderId(sellOrderId);
        trade.setPrice(price);
        trade.setQuantity(quantity);
        trade.setTimestamp(LocalDateTime.now());
        tradeRepository.save(trade);
    }
    public Optional<Order> findOptionalByClientOrderId(String clientOrderId) {
        return orderRepository.findByClientOrderId(clientOrderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
