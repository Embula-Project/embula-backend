package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.DineInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DineInOrderRepository extends JpaRepository<DineInOrder, Long> {
    Optional<DineInOrder> findByOrder_OrderId(Long orderId);
}
