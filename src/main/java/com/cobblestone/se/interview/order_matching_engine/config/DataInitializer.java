package com.cobblestone.se.interview.order_matching_engine.config;

import com.cobblestone.se.interview.order_matching_engine.model.Order;
import com.cobblestone.se.interview.order_matching_engine.model.Trade;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderStatus;
import com.cobblestone.se.interview.order_matching_engine.model.enums.OrderType;
import com.cobblestone.se.interview.order_matching_engine.repository.OrderRepository;
import com.cobblestone.se.interview.order_matching_engine.repository.TradeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(OrderRepository orderRepository, TradeRepository tradeRepository) {
        return args -> {
            Order natGasBuy = new Order();
            natGasBuy.setClientOrderId(UUID.randomUUID().toString());
            natGasBuy.setSymbol("NATGAS");
            natGasBuy.setPrice(2.50);
            natGasBuy.setQuantity(100);
            natGasBuy.setType(OrderType.BUY);
            natGasBuy.setStatus(OrderStatus.FILLED);
            orderRepository.save(natGasBuy);

            Order natGasSell = new Order();
            natGasSell.setClientOrderId(UUID.randomUUID().toString());
            natGasSell.setSymbol("NATGAS");
            natGasSell.setPrice(2.50);
            natGasSell.setQuantity(0);
            natGasSell.setType(OrderType.SELL);
            natGasSell.setStatus(OrderStatus.FILLED);
            orderRepository.save(natGasSell);

            tradeRepository.save(new Trade(
                    null,
                    "NATGAS",
                    natGasBuy.getId().toString(),
                    natGasSell.getId().toString(),
                    2.50,
                    100,
                    LocalDateTime.now()
            ));

            Order brentBuy = new Order();
            brentBuy.setClientOrderId(UUID.randomUUID().toString());
            brentBuy.setSymbol("BRENT");
            brentBuy.setPrice(76.80);
            brentBuy.setQuantity(50);
            brentBuy.setType(OrderType.BUY);
            brentBuy.setStatus(OrderStatus.FILLED);
            orderRepository.save(brentBuy);

            Order brentSell = new Order();
            brentSell.setClientOrderId(UUID.randomUUID().toString());
            brentSell.setSymbol("BRENT");
            brentSell.setPrice(76.80);
            brentSell.setQuantity(0);
            brentSell.setType(OrderType.SELL);
            brentSell.setStatus(OrderStatus.FILLED);
            orderRepository.save(brentSell);

            tradeRepository.save(new Trade(
                    null,
                    "BRENT",
                    brentBuy.getId().toString(),
                    brentSell.getId().toString(),
                    76.80,
                    50,
                    LocalDateTime.now()
            ));

            Order wtiBuy = new Order();
            wtiBuy.setClientOrderId(UUID.randomUUID().toString());
            wtiBuy.setSymbol("WTI");
            wtiBuy.setPrice(72.10);
            wtiBuy.setQuantity(60);
            wtiBuy.setType(OrderType.BUY);
            wtiBuy.setStatus(OrderStatus.PENDING);
            orderRepository.save(wtiBuy);
        };
    }
}