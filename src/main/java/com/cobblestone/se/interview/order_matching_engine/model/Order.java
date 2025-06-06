package com.cobblestone.se.interview.order_matching_engine.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Order(String symbol, double price, int quantity, String type,String clientOrderId) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.type = type.toUpperCase();
        this.status = "PENDING";
        this.clientOrderId = clientOrderId;
    }

    private String symbol;
    private double price;
    private int quantity;
    private String type; // BUY or SELL
    private String status; // PENDING, PARTIALLY_FILLED, FILLED
    @Column(unique = true)
    private String clientOrderId;
    @CreationTimestamp
    private LocalDateTime createdAt;

}