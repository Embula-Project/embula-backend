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
    private String customerEmail;
    private String orderDescription;
    private String orderType;
    private List<OrderFoodItemRequest> orderFoodItems;

    // Dine In / Take Away scheduling (ISO-8601: "yyyy-MM-dd" / "HH:mm")
    private String scheduledDate;
    private String scheduledTime;

    // Delivery details (not persisted to the customer's profile — order-specific only)
    private String deliveryAddress;
    private String deliveryPhone;
}
