package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT COUNT(p) FROM Payment p")
    long countAllPayments();
}
