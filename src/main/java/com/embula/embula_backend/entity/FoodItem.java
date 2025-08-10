package com.embula.embula_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "food_item")
public class FoodItem {

    @Id
    private String itemId;

    private String itemName;

    private List<String> ingredients;

    private String type; //need an enum

    private String description;

    private double price;

    private String portionSize;

    private Binary image;


}
