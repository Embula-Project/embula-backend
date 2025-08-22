package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodItemMappers {
    List<FoodItemToMenuDTO> getAllFoodItems (List<FoodItem> foodItem);
    FoodItem updateFoodItem(FoodItemUpdateDTO foodItemUpdateDTO, @MappingTarget FoodItem foodItem);
}
