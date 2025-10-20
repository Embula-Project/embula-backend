package com.embula.embula_backend.repository;


import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@EnableJpaRepositories
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    FoodItem findFoodItemsByItemId(long ItemId);

}
