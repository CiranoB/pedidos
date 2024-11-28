package com.fiap.pedidos.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.pedidos.exceptions.InvalidEmailAddress;
import com.fiap.pedidos.exceptions.InvalidProductQuantity;
import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.records.UpdateOrderRecord;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(this.orderService.getByOrderId(orderId));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrderByEmail(@RequestParam(name = "email") String email){
        return ResponseEntity.ok(this.orderService.getOrderByEmail(email));
    }

    @PatchMapping("/update_status")
    public ResponseEntity<Optional<Order>> updateOrderStatus (@RequestBody UpdateOrderRecord updateOrderRecord){
        return ResponseEntity.ok(this.orderService.updateOrderPaymentStatus(updateOrderRecord.orderId(), updateOrderRecord.paymentStatus()));
    }

}
