package com.cobblestone.se.interview.order_matching_engine.dto;


import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.antlr.v4.runtime.misc.NotNull;

public class OrderRequestDTO {
    @NotBlank
    public String symbol;

    @Positive
    public double price;

    @Positive
    public int quantity;

    @NotNull
    public OrderType type;

    public String clientOrderId;
}
