package com.fiap.pedidos.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.pedidos.exceptions.InvalidEmailAddress;
import com.fiap.pedidos.exceptions.InvalidProductQuantity;
import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public List<Order> getAll() {
        return this.orderService.getAll();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrderRecord orderRecord) throws InvalidProductQuantity, InvalidEmailAddress {
        try {
            return ResponseEntity.ok(this.orderService.saveOrder(orderRecord));
        } catch (InvalidProductQuantity e) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("Message", e.getMessage(), "Error", "Invalid Product Quantities"));
        } catch (InvalidEmailAddress e){
            return ResponseEntity.unprocessableEntity()
            .body(Map.of("Message", e.getMessage(), "Error", "Invalid Email"));
        }
        
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getOrderByEmail(String email){
        return ResponseEntity.ok(this.orderService.getOrderByEmail(email));
    }
}
