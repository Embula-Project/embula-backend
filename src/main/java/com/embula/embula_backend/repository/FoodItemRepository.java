package com.embula.embula_backend.repository;


import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    FoodItem findFoodItemsByItemId(Long ItemId);

}
