package com.embula.embula_backend.dto.request;
import com.embula.embula_backend.entity.enums.FoodItemType;
import com.embula.embula_backend.entity.enums.FoodPortionSize;
import com.embula.embula_backend.entity.enums.FoodItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodItemUpdateDTO {
    private String itemName;
    private List<String> ingredients;
    private FoodItemType type;
    private String description;
    private double price;
    private FoodPortionSize portionSize;
    private byte[] image;

}
