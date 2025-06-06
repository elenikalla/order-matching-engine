package com.cobblestone.se.interview.order_matching_engine.model;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
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
    public Order(String symbol, double price, int quantity, OrderType type,String clientOrderId,OrderStatus status) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.clientOrderId = clientOrderId;
    }
    private String symbol;
    private double price;
    private int quantity;
    private OrderType type;
    private OrderStatus status;
    @Column(unique = true)
    private String clientOrderId;
    @CreationTimestamp
    private LocalDateTime createdAt;
}