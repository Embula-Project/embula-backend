package com.embula.embula_backend.dto.paginated;

import com.embula.embula_backend.dto.response.FoodItemToMenuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedAllFoodItems {
    List<FoodItemToMenuDTO> list;
    private long totalFoodItems;
}
