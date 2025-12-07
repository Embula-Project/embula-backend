package com.embula.embula_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFoodItemRequest {

    private String itemName;
    private int qty;
    private double amount;
    private Long foodItemId;
}
