package com.embula.embula_backend.services.impl;
import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.repository.FoodItemRepository;
import com.embula.embula_backend.services.FoodItemService;
import com.embula.embula_backend.util.mappers.FoodItemMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodItemServiceIMPL implements FoodItemService {


    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemMappers foodItemMappers;


    @Override
    public String saveFoodItem(FoodItem foodItem){
        foodItemRepository.save(foodItem);
        return foodItem.getItemId() + " Saved Successfully";
    }


    @Override
    public List<FoodItemToMenuDTO> getAllFoodItems(){
        List<FoodItem> foodItem = foodItemRepository.findAll();
        List<FoodItemToMenuDTO> foodItemToMenuDTO = new ArrayList<>();
        foodItemToMenuDTO = foodItemMappers.getAllFoodItems(foodItem);
        return foodItemToMenuDTO;

    }

    @Override
    public String updateFoodItem (String ItemId, FoodItemUpdateDTO foodItemUpdateDTO){
        if(foodItemRepository.existsById(ItemId)){
            FoodItem foodItem = foodItemRepository.getFoodItemsByItemId(ItemId);
            foodItemMappers.updateFoodItem(foodItemUpdateDTO , foodItem);
            foodItemRepository.save(foodItem);
            return foodItem.getItemId() + " Updated Successfully";
        }else{
            throw new RuntimeException("Item Not Found");
        }
    }
}
