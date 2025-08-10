package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.services.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/fooditem")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;


    @PostMapping(path="saveItem")
    public String saveFoodItem (FoodItem foodItem){
        String message = foodItemService.saveFoodItem(foodItem);
        return message;
    }


    @GetMapping(path="getAllFoodItems")
    public List<FoodItemToMenuDTO> getAllFoodItems (){
        List<FoodItemToMenuDTO> foodItemToMenuDTO = foodItemService.getAllFoodItems();
        return foodItemToMenuDTO;
    }

    @PutMapping(
            path="updateFoodItem",
            params="itemId"
    )
    public String updateFoodItem (@RequestParam String itemId, @RequestBody FoodItemUpdateDTO foodItemUpdateDTO){
        String message = foodItemService.updateFoodItem(itemId, foodItemUpdateDTO);
        return message;
    }
}
