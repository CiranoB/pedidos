package com.fiap.pedidos.models;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.pedidos.enums.PaymentStatus;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    private String idOrder;

    @Email
    private String userEmail;

    private LocalDateTime createDate;

    private Map<String, Integer> productQuantities;

    private PaymentStatus paymentStatus;
}
