package com.embula.embula_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long amount;
    private Long quantity;
    private String orderName;
    private String currency;
    private String customerId;
    private String orderDescription;
    private String orderType;
    private List<OrderFoodItemRequest> orderFoodItems;

}
