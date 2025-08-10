package com.embula.embula_backend.repository;


import com.embula.embula_backend.entity.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
    FoodItem getFoodItemsByItemId(String ItemId);

}
