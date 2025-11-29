package com.embula.embula_backend.dto.request;
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
    private String type;
    private String description;
    private double price;
    private String portionSize;
    private byte[] image;
}
