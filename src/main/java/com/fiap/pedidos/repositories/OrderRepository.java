package com.fiap.pedidos.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.pedidos.models.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{

}
