package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.TakeAwayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TakeAwayOrderRepository extends JpaRepository<TakeAwayOrder, Long> {
    Optional<TakeAwayOrder> findByOrder_OrderId(Long orderId);
}
