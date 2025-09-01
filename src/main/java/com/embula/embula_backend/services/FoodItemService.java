package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.FoodItemDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllFoodItems;
import com.embula.embula_backend.dto.request.FoodItemUpdateDTO;
import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import com.embula.embula_backend.dto.response.ViewFoodItemDTO;
import com.embula.embula_backend.entity.FoodItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FoodItemService {

    public String saveFoodItem(FoodItemDTO foodItemDTO, MultipartFile imageFile) throws IOException;
    public List<FoodItemToMenuDTO> getAllFoodItems();
    public String updateFoodItem(String ItemId, FoodItemUpdateDTO foodItemUpdateDTO);
    public PaginatedAllFoodItems getAllFoodItemsPaginated (int page, int size);
    public ViewFoodItemDTO viewFoodItem(String itemId);
}
