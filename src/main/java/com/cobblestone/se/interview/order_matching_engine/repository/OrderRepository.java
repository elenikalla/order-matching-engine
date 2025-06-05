package com.cobblestone.se.interview.order_matching_engine.repository;


import com.cobblestone.se.interview.order_matching_engine.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySymbolIgnoreCase(String symbol);
    Optional<Order> findByClientOrderId(String clientOrderId);

}
