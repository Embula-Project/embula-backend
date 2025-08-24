package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.paginated.PaginatedAllFoodItems;
import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.dto.response.ViewFoodItemDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.services.FoodItemService;
import com.embula.embula_backend.util.StandardResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/fooditem")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;


    @PostMapping(path="saveItem")
    public ResponseEntity<StandardResponse> saveFoodItem (FoodItem foodItem){
        String message = foodItemService.saveFoodItem(foodItem);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }


    @GetMapping(
            path="getAllFoodItems",
            params= {"page", "size"}
    )
    public ResponseEntity<StandardResponse> getAllFoodItems (
            @RequestParam(value="page") int page,
            @RequestParam(value="size")int size
    ){
//        List<FoodItemToMenuDTO> foodItemToMenuDTO = foodItemService.getAllFoodItems();
        PaginatedAllFoodItems paginatedAllFoodItems = foodItemService.getAllFoodItemsPaginated(page,size);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", paginatedAllFoodItems),
                HttpStatus.OK
        );
        return responseEntity;
    }

    @PutMapping(
            path="updateFoodItem",
            params="itemId"
    )
    public  ResponseEntity<StandardResponse> updateFoodItem (@RequestParam String itemId, @RequestBody FoodItemUpdateDTO foodItemUpdateDTO){
        String message = foodItemService.updateFoodItem(itemId, foodItemUpdateDTO);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }

    @GetMapping(
            path="viewFoodItem",
            params="itemId"
    )
    public ResponseEntity<StandardResponse> viewFoodItemDetails (@RequestParam String itemId){
        ViewFoodItemDTO viewFoodItemDTO = foodItemService.viewFoodItem(itemId);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", viewFoodItemDTO),
                HttpStatus.OK
        );
        return responseEntity;
    }

}
