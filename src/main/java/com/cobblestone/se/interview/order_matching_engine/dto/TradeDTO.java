package com.cobblestone.se.interview.order_matching_engine.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TradeDTO {

    public Long id;
    public String symbol;
    public int quantity;
    public double price;
    public String buyOrderId;
    public String sellOrderId;
    public LocalDateTime timestamp;

    public TradeDTO(Long id, String symbol, int quantity, double price,
                    String buyOrderId, String sellOrderId, LocalDateTime timestamp) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.timestamp = timestamp;
    }
}