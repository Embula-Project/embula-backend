package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

}
