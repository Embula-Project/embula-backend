package com.embula.embula_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewFoodItemDTO {

    private String itemId;
    private String itemName;
    private List<String> ingredients;
    private String type; //need an enum
    private String description;
    private double price;
    private String portionSize;
    private byte[] image;
}
