package com.cobblestone.se.interview.order_matching_engine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "trades")
public class Trade {
    @Id
    @GeneratedValue
    private Long id;
    private String symbol;
    private String buyOrderId;
    private String sellOrderId;
    private double price;
    private int quantity;
    private LocalDateTime timestamp;
}