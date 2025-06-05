package com.cobblestone.se.interview.order_matching_engine.controller;


import com.cobblestone.se.interview.order_matching_engine.dto.PositionDTO;
import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import com.cobblestone.se.interview.order_matching_engine.service.PositionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public List<PositionDTO> getPositions() {
        return positionService.calculatePositions();
    }
}