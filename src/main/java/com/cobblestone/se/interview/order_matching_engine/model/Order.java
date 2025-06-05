package com.cobblestone.se.interview.order_matching_engine.model;

import jakarta.persistence.*;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double price;
    private int quantity;
    private String type; // BUY or SELL
    private String status; // PENDING, PARTIALLY_FILLED, FILLED

    public Order() {}

    public Order(String symbol, double price, int quantity, String type) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.type = type.toUpperCase();
        this.status = "PENDING";
    }

    // Getters and setters omitted for brevity
}
