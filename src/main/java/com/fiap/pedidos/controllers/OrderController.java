package com.fiap.pedidos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.services.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public List<Order> getAll(){
        return this.orderService.getAll();
    }

    @PostMapping("/save")
    public Order save(@RequestBody OrderRecord orderRecord){
        return this.orderService.saveOrder(orderRecord);
    }
}
