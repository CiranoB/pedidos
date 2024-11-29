package com.fiap.pedidos.infra;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.fiap.pedidos.configuration.OrderProperties;
import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.services.OrderEventGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventGatewayWithStreamBridge implements OrderEventGateway {

    private final StreamBridge streamBridge;
    private final OrderProperties orderProperties;

    public void sendOrderCreatedEvent (Order order){
        log.info("Ordem Criada: " + order.getIdOrder());
        streamBridge.send(orderProperties.getOrderCreatedChannel(), order);
    }


}
