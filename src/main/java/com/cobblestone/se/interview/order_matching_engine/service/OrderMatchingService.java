package com.cobblestone.se.interview.order_matching_engine.service;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
import com.cobblestone.se.interview.order_matching_engine.model.Trade;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderMatchingService {

    private static final Logger logger = LoggerFactory.getLogger(OrderMatchingService.class);

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
        order.setStatus(OrderStatus.PENDING);
        order.setClientOrderId(dto.clientOrderId);
        return addOrder(order);
    }

    @Transactional
    public Order addOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        if (order.getType() == OrderType.BUY) {
            matchBuyOrder(savedOrder);
        } else {
            matchSellOrder(savedOrder);
        }
        return savedOrder;
    }

    private void matchBuyOrder(Order buyOrder) {
        boolean matched = false;

        List<Order> sells = orderRepository
                .findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceAscCreatedAtAsc(
                        buyOrder.getSymbol(), OrderType.SELL, OrderStatus.FILLED, 0
                );

        for (Order sell : sells) {
            if (buyOrder.getQuantity() == 0) break;
            if (sell.getPrice() <= buyOrder.getPrice()) {
                int tradedQty = Math.min(buyOrder.getQuantity(), sell.getQuantity());

                buyOrder.setQuantity(buyOrder.getQuantity() - tradedQty);
                sell.setQuantity(sell.getQuantity() - tradedQty);

                recordTrade(buyOrder.getSymbol(), buyOrder.getId().toString(), sell.getId().toString(), sell.getPrice(), tradedQty);

                sell.setStatus(sell.getQuantity() == 0 ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED);
                orderRepository.save(sell);

                matched = true;
            }
        }

        if (!matched) {
            buyOrder.setStatus(OrderStatus.PENDING);
        } else if (buyOrder.getQuantity() > 0) {
            buyOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
        } else {
            buyOrder.setStatus(OrderStatus.FILLED);
        }

        orderRepository.save(buyOrder);
    }
    private void matchSellOrder(Order sellOrder) {
        boolean matched = false;

        List<Order> buys = orderRepository
                .findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceDescCreatedAtAsc(
                        sellOrder.getSymbol(), OrderType.BUY, OrderStatus.FILLED, 0
                );

        for (Order buy : buys) {
            if (sellOrder.getQuantity() == 0) break;
            if (buy.getPrice() >= sellOrder.getPrice()) {
                int tradedQty = Math.min(sellOrder.getQuantity(), buy.getQuantity());

                sellOrder.setQuantity(sellOrder.getQuantity() - tradedQty);
                buy.setQuantity(buy.getQuantity() - tradedQty);

                recordTrade(sellOrder.getSymbol(), buy.getId().toString(), sellOrder.getId().toString(), buy.getPrice(), tradedQty);

                buy.setStatus(buy.getQuantity() == 0 ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED);
                orderRepository.save(buy);

                matched = true;
            }
        }

        if (!matched) {
            sellOrder.setStatus(OrderStatus.PENDING);
        } else if (sellOrder.getQuantity() > 0) {
            sellOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
        } else {
            sellOrder.setStatus(OrderStatus.FILLED);
        }

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

        logger.info("Trade recorded: {} {} @ {}", symbol, quantity, price);
    }

    public Optional<Order> findOptionalByClientOrderId(String clientOrderId) {
        return orderRepository.findByClientOrderId(clientOrderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
