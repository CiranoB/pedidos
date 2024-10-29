package com.fiap.pedidos.models;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fiap.pedidos.enums.PaymentStatus;
import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
public class Order {
    
    @Id
    private String idOrder;

    @NonNull
    private String userEmail;

    @NonNull
    private LocalDateTime createDate;

    @NonNull
    private Map<String, Integer> productQuantities;

    @NonNull
    private PaymentStatus paymentStatus;
}
