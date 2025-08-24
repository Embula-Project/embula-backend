package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
