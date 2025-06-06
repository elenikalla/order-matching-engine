package com.cobblestone.se.interview.order_matching_engine.dto;


import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;

public class OrderRequestDTO {
    public String symbol;
    public double price;
    public int quantity;
    public OrderType type;
    public String clientOrderId;

}
