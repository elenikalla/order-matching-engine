package com.cobblestone.se.interview.order_matching_engine.controller;

import com.cobblestone.se.interview.order_matching_engine.dto.TradeDTO;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {
    private final TradeRepository tradeRepository;

    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @GetMapping
    public List<TradeDTO> getAllTrades() {
        return tradeRepository.findAll().stream()
                .map(trade -> new TradeDTO(
                        trade.getId(),
                        trade.getSymbol(),
                        trade.getQuantity(),
                        trade.getPrice(),
                        trade.getBuyOrderId(),
                        trade.getSellOrderId(),
                        trade.getCreatedAt()
                ))
                .toList();
    }
}