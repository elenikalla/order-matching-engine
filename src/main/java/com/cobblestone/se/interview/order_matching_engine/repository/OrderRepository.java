package com.cobblestone.se.interview.order_matching_engine.repository;

import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceAscCreatedAtAsc(
            String symbol, OrderType type, OrderStatus status, int quantity);

    List<Order> findBySymbolIgnoreCaseAndTypeAndStatusNotAndQuantityGreaterThanOrderByPriceDescCreatedAtAsc(
            String symbol, OrderType type, OrderStatus status, int quantity);
}
