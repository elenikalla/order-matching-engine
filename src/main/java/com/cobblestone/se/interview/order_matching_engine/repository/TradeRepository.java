package com.cobblestone.se.interview.order_matching_engine.repository;

import com.cobblestone.se.interview.order_matching_engine.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findBySymbol(String symbol);
}