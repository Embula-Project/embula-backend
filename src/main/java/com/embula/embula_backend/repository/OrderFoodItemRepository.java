package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.OrderFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFoodItemRepository extends JpaRepository<OrderFoodItem, Integer> {
}
