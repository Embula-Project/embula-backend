package com.embula.embula_backend.dto.request;

import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestOrderFoodItemSaveDTO {

    private String itemName;
    private int qty;
    private double amount;
    private Long orders;
    private Long foodItems;


}
