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
    public String updateFoodItem (long itemId, FoodItemUpdateDTO foodItemUpdateDTO, MultipartFile imageFile) throws IOException {
        if(foodItemRepository.existsById(itemId)){
            FoodItem foodItem = foodItemRepository.findFoodItemsByItemId(itemId);

            // Update basic fields from DTO
            if(foodItemUpdateDTO.getItemName() != null) {
                foodItem.setItemName(foodItemUpdateDTO.getItemName());
            }
            if(foodItemUpdateDTO.getIngredients() != null) {
                foodItem.setIngredients(foodItemUpdateDTO.getIngredients());
            }
            if(foodItemUpdateDTO.getType() != null) {
                foodItem.setType(foodItemUpdateDTO.getType());
            }
            if(foodItemUpdateDTO.getDescription() != null) {
                foodItem.setDescription(foodItemUpdateDTO.getDescription());
            }
            if(foodItemUpdateDTO.getPrice() > 0) {
                foodItem.setPrice(foodItemUpdateDTO.getPrice());
            }
            if(foodItemUpdateDTO.getPortionSize() != null) {
                foodItem.setPortionSize(foodItemUpdateDTO.getPortionSize());
            }

            // Update image only if a new image file is provided
            if(imageFile != null && !imageFile.isEmpty()) {
                foodItem.setImageName(imageFile.getOriginalFilename());
                foodItem.setImageType(imageFile.getContentType());
                foodItem.setImageData(imageFile.getBytes());
            }

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
    public ViewFoodItemDTO viewFoodItem(long itemId){
        if(foodItemRepository.existsById(itemId)){
            FoodItem foodItem = foodItemRepository.findFoodItemsByItemId(itemId);
            ViewFoodItemDTO viewFoodItemDTO = foodItemMappers.FoodItemToViewFoodItemDTO(foodItem);
            return viewFoodItemDTO;
        }else{
            throw new NotFoundException("Item Not Found");
        }
    }

    @Override
    public String deleteFoodItem(Long itemId){
        if(foodItemRepository.existsById(itemId)){
            foodItemRepository.deleteById(itemId);
            return "Item "+ itemId + " Deleted Successfully";
        }else{
            throw new NotFoundException("Item Not Found");
        }
    }
}
