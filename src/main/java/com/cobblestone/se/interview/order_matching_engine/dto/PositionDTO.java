package com.cobblestone.se.interview.order_matching_engine.dto;

public class PositionDTO {
    public String symbol;
    public int buyQty;
    public double buyTotal;
    public int sellQty;
    public double sellTotal;

    public int netPosition;
    public double avgBuyPrice;
    public double avgSellPrice;
    public double pnl;

    public PositionDTO(String symbol) {
        this.symbol = symbol;
    }

    public void finalizeMetrics() {
        this.netPosition = buyQty - sellQty;
        this.avgBuyPrice = buyQty == 0 ? 0 : buyTotal / buyQty;
        this.avgSellPrice = sellQty == 0 ? 0 : sellTotal / sellQty;
        int matchedQty = Math.min(buyQty, sellQty);
        this.pnl = (avgSellPrice - avgBuyPrice) * matchedQty;
    }
}