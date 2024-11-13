package com.fiap.pedidos.records;

import com.fiap.pedidos.enums.PaymentStatus;

public record UpdateOrderRecord(
    String orderId,
    PaymentStatus paymentStatus
) {

}
