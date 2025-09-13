package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, String> {

}
