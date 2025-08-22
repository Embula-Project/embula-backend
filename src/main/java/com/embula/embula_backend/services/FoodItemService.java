package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.paginated.PaginatedAllFoodItems;
import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;

import java.util.List;

public interface FoodItemService {

    public String saveFoodItem(FoodItem foodItem);
    public List<FoodItemToMenuDTO> getAllFoodItems();
    public String updateFoodItem(String ItemId, FoodItemUpdateDTO foodItemUpdateDTO);
    public PaginatedAllFoodItems getAllFoodItemsPaginated (int page, int size);
}
