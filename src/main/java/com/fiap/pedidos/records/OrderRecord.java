package com.fiap.pedidos.records;

import java.util.Map;

public record OrderRecord(
    String userEmail,
    Map<String, Integer> productQuantities
) {

}
