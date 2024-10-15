package com.fiap.pedidos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiap.pedidos.enums.PaymentStatus;
import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.repositories.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAll(){
        return this.orderRepository.findAll();
    }

    public Order saveOrder(OrderRecord orderRecord){
        Order order = new Order();
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setProductQuantities(orderRecord.productQuantities());
        order.setUserEmail(orderRecord.userEmail());
        this.orderRepository.save(order);
        return order;
    }

}
