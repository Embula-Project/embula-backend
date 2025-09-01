package com.embula.embula_backend.services.impl;
import com.embula.embula_backend.dto.FoodItemDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllFoodItems;
import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.dto.response.ViewFoodItemDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.repository.FoodItemRepository;
import com.embula.embula_backend.services.FoodItemService;
import com.embula.embula_backend.util.mappers.FoodItemMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodItemServiceIMPL implements FoodItemService {


    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemMappers foodItemMappers;


    @Override
    public String saveFoodItem(FoodItemDTO foodItemDTO , MultipartFile imageFile) throws IOException {
        foodItemDTO.setImageName(imageFile.getOriginalFilename());
        foodItemDTO.setImageType(imageFile.getContentType());
        foodItemDTO.setImageData(imageFile.getBytes());
        FoodItem foodItem = foodItemMappers.foodItemDTOToFoodItem(foodItemDTO);
        foodItemRepository.save(foodItem);
        return foodItem.getItemId() + " Saved Successfully";
    }


    @Override
    public List<FoodItemToMenuDTO> getAllFoodItems(){
        List<FoodItem> foodItem = foodItemRepository.findAll();
        if(foodItem.isEmpty()){
            throw new NotFoundException("There are no items to find.");
        }
        List<FoodItemToMenuDTO> foodItemToMenuDTO = new ArrayList<>();
        foodItemToMenuDTO = foodItemMappers.getAllFoodItems(foodItem);
        return foodItemToMenuDTO;

    }

    @Override
    public String updateFoodItem (String ItemId, FoodItemUpdateDTO foodItemUpdateDTO){
        if(foodItemRepository.existsById(ItemId)){
            FoodItem foodItem = foodItemRepository.findFoodItemsByItemId(ItemId);
            foodItemMappers.updateFoodItem(foodItemUpdateDTO , foodItem);
            foodItemRepository.save(foodItem);
            return foodItem.getItemId() + " Updated Successfully";
        }else{
            throw new NotFoundException("Item Not Found");
        }
    }

    @Override
    public PaginatedAllFoodItems getAllFoodItemsPaginated (int page, int size){
        Page<FoodItem> foodItems = foodItemRepository.findAll(PageRequest.of(page,size));
        long count = foodItems.getTotalElements();
        if(foodItems.isEmpty()){
            throw new NotFoundException("There are no items to find.");
        }
        PaginatedAllFoodItems paginatedAllFoodItems = new PaginatedAllFoodItems(
                foodItemMappers.PageToList(foodItems), count
        );
        return paginatedAllFoodItems;
    }


    @Override
    public ViewFoodItemDTO viewFoodItem(String itemId){
        if(foodItemRepository.existsById(itemId)){
            FoodItem foodItem = foodItemRepository.findFoodItemsByItemId(itemId);
            ViewFoodItemDTO viewFoodItemDTO = foodItemMappers.FoodItemToViewFoodItemDTO(foodItem);
            return viewFoodItemDTO;
        }else{
            throw new NotFoundException("Item Not Found");
        }
    }
}
