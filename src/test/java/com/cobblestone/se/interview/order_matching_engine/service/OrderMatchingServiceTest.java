package com.cobblestone.se.interview.order_matching_engine.service;

import com.cobblestone.se.interview.order_matching_engine.dto.OrderRequestDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMatchingServiceTest {

    private OrderRepository orderRepository;
    private TradeRepository tradeRepository;
    private OrderMatchingService service;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        tradeRepository = mock(TradeRepository.class);
        service = new OrderMatchingService(orderRepository, tradeRepository);
    }

    @Test
    void testHandleBuyOrder_SavesCorrectOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.clientOrderId = "client-123";
        dto.symbol = "AAPL";
        dto.price = 150.0;
        dto.quantity = 100;
        dto.type = OrderType.BUY;

        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceAscCreatedAtAsc(
                eq("AAPL"), eq(OrderType.SELL), any(), anyInt())).thenReturn(Collections.emptyList());

        Order saved = service.handle(dto);

        assertEquals(OrderStatus.PENDING, saved.getStatus());
        assertEquals("AAPL", saved.getSymbol());
        assertEquals(100, saved.getQuantity());
    }

    @Test
    void testHandleSellOrder_MatchesCorrectlyWhenBuyExists() {
        Order sellOrder = new Order();
        sellOrder.setSymbol("AAPL");
        sellOrder.setPrice(150.0);
        sellOrder.setQuantity(100);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setStatus(OrderStatus.PENDING);

        Order buyOrder = new Order();
        buyOrder.setId(1L); // Simulate DB-assigned ID
        buyOrder.setSymbol("AAPL");
        buyOrder.setPrice(155.0);
        buyOrder.setQuantity(100);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceDescCreatedAtAsc(
                eq("AAPL"), eq(OrderType.BUY), any(), anyInt()))
                .thenReturn(Collections.singletonList(buyOrder));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) order.setId(99L); // Simulate DB assigning ID
            return order;
        });

        service.addOrder(sellOrder);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, atLeastOnce()).save(orderCaptor.capture());

        boolean hasFilledStatus = orderCaptor.getAllValues().stream()
                .anyMatch(o -> o.getStatus() == OrderStatus.FILLED || o.getStatus() == OrderStatus.PARTIALLY_FILLED);

        assertTrue(hasFilledStatus, "Expected at least one order to be FILLED or PARTIALLY_FILLED");
    }

    @Test
    void testHandleSellOrder_PartiallyMatchesWithSmallerBuyOrder() {
        Order sellOrder = new Order();
        sellOrder.setId(200L);
        sellOrder.setSymbol("AAPL");
        sellOrder.setPrice(150.0);
        sellOrder.setQuantity(100);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setStatus(OrderStatus.PENDING);

        Order buyOrder = new Order();
        buyOrder.setId(201L);
        buyOrder.setSymbol("AAPL");
        buyOrder.setPrice(155.0); // Better price
        buyOrder.setQuantity(40); // Less than sell order
        buyOrder.setType(OrderType.BUY);
        buyOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceDescCreatedAtAsc(
                eq("AAPL"), eq(OrderType.BUY), any(), anyInt()))
                .thenReturn(List.of(buyOrder));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) order.setId(new Random().nextLong());
            return order;
        });

        service.addOrder(sellOrder);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, atLeast(1)).save(orderCaptor.capture());

        boolean sellOrderPartiallyFilled = orderCaptor.getAllValues().stream()
                .anyMatch(o -> o.getId().equals(200L) && o.getStatus() == OrderStatus.PARTIALLY_FILLED);

        boolean buyOrderFilled = orderCaptor.getAllValues().stream()
                .anyMatch(o -> o.getId().equals(201L) && o.getStatus() == OrderStatus.FILLED);

        assertTrue(sellOrderPartiallyFilled, "Sell order should be partially filled");
        assertTrue(buyOrderFilled, "Buy order should be fully filled");
    }
    @Test
    void testHandleSellOrder_MatchesAgainstMultipleBuyOrders() {
        Order sellOrder = new Order();
        sellOrder.setId(300L);
        sellOrder.setSymbol("AAPL");
        sellOrder.setPrice(150.0);
        sellOrder.setQuantity(100);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setStatus(OrderStatus.PENDING);

        Order buyOrder1 = new Order();
        buyOrder1.setId(301L);
        buyOrder1.setSymbol("AAPL");
        buyOrder1.setPrice(155.0);
        buyOrder1.setQuantity(60);
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setStatus(OrderStatus.PENDING);

        Order buyOrder2 = new Order();
        buyOrder2.setId(302L);
        buyOrder2.setSymbol("AAPL");
        buyOrder2.setPrice(152.0);
        buyOrder2.setQuantity(40);
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setStatus(OrderStatus.PENDING);

        when(orderRepository.findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceDescCreatedAtAsc(
                eq("AAPL"), eq(OrderType.BUY), any(), anyInt()))
                .thenReturn(List.of(buyOrder1, buyOrder2));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) order.setId(new Random().nextLong());
            return order;
        });

        service.addOrder(sellOrder);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, atLeast(1)).save(orderCaptor.capture());

        boolean sellOrderFilled = orderCaptor.getAllValues().stream()
                .anyMatch(o -> o.getId().equals(300L) && o.getStatus() == OrderStatus.FILLED);

        boolean allBuysFilled = orderCaptor.getAllValues().stream()
                .filter(o -> o.getId().equals(301L) || o.getId().equals(302L))
                .allMatch(o -> o.getStatus() == OrderStatus.FILLED);

        assertTrue(sellOrderFilled, "Sell order should be fully filled");
        assertTrue(allBuysFilled, "Both buy orders should be filled");
    }
}