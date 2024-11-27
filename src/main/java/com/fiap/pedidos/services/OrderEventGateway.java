package com.fiap.pedidos.services;

import com.fiap.pedidos.models.Order;

public interface OrderEventGateway {

    void sendOrderCreatedEvent(Order order);
    
}
