package com.fiap.pedidos.mock;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fiap.pedidos.enums.PaymentStatus;
import com.fiap.pedidos.models.Order;

public class OrderMock {

    public static Order build() {
        final var order = new Order();
        order.setIdOrder(UUID.randomUUID().toString());
        order.setUserEmail("teste@email.com");
        order.setCreateDate(LocalDateTime.now());
        order.setProductQuantities(Map.of("teste", 1));
        order.setPaymentStatus(PaymentStatus.PENDING);
        return order;

    }

}
