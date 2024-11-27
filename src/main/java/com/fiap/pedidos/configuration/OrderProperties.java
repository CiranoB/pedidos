package com.fiap.pedidos.configuration;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class OrderProperties {

    private String orderCreatedChannel = "orderCreatedSupplier-out-0";
    
}
