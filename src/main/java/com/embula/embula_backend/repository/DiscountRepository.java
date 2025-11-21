package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByIsActiveTrue();

    List<Discount> findByValidFromLessThanEqualAndValidToGreaterThanEqual(
            LocalDate today1, LocalDate today2
    );
}