package com.embula.embula_backend.repository;

import com.embula.embula_backend.entity.OrderFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrderFoodItemRepository extends JpaRepository<OrderFoodItem, Integer> {
}
