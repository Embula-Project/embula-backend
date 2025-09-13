package com.embula.embula_backend.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    private Long itemId;
    private String itemName;
    private List<String> ingredients;
    private String type;
    private String description;
    private double price;
    private String portionSize;
    @JsonIgnore
    private String imageName;

    @JsonIgnore
    private String imageType;

    @JsonIgnore
    private byte[] imageData;

}
