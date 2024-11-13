package com.fiap.pedidos.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fiap.pedidos.enums.PaymentStatus;
import com.fiap.pedidos.exceptions.InvalidEmailAddress;
import com.fiap.pedidos.exceptions.InvalidProductQuantity;
import com.fiap.pedidos.models.Order;
import com.fiap.pedidos.records.OrderRecord;
import com.fiap.pedidos.repositories.OrderRepository;

@Service
public class OrderService {

    private final MongoTemplate mongoTemplate;

    public OrderService (MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private OrderRepository orderRepository;

    private void verifyProductQuantities(Map<String, Integer> productQuantities) throws InvalidProductQuantity{
        for(var entry: productQuantities.entrySet()){
            if(entry.getValue()<1){
                throw new InvalidProductQuantity("Product " + entry.getKey() + " has less than 1 quantity!");
            }
        }
    } 
    
    private void verifyEmail(String email) throws InvalidEmailAddress {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailAddress("Invalid email address: " + email);
        }   
    }

    public List<Order> getAll(){
        return this.orderRepository.findAll();
    }

    public Optional<Order> getByOrderId(String orderId){
        return this.orderRepository.findById(orderId);
    }

    public Order saveOrder(OrderRecord orderRecord) throws InvalidProductQuantity, InvalidEmailAddress{
        verifyProductQuantities(orderRecord.productQuantities());
        verifyEmail(orderRecord.userEmail());
        Order order = new Order();
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setProductQuantities(orderRecord.productQuantities());
        order.setUserEmail(orderRecord.userEmail());
        order.setCreateDate(LocalDateTime.now());
        this.orderRepository.save(order);
        return order;
    }

    public List<Order> getOrderByEmail(String email){
        Query query = new Query(Criteria.where("userEmail").is(email));
        return mongoTemplate.find(query, Order.class);
    }

    public Optional<Order> updateOrderPaymentStatus(String orderId, PaymentStatus newStatus){
        Optional<Order> optionalOrder = this.getByOrderId(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setPaymentStatus(newStatus);
            this.orderRepository.save(order);
            return Optional.of(order);
        }
        return Optional.empty();
        }

}
