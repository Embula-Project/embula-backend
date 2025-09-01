package com.embula.embula_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodItemToMenuDTO {
    private String itemName;
    private List<String> ingredients;
    private String type;
    private String description;
    private double price;
    private String portionSize;
    private String imageName;
    private String imageType;
    private byte[] imageData;

}
