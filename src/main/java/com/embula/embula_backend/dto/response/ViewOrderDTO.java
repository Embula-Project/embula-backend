package com.embula.embula_backend.dto.response;


import com.embula.embula_backend.entity.enums.OrderStatus;
import com.embula.embula_backend.entity.enums.OrderType;
import com.embula.embula_backend.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewOrderDTO {

    private String  orderName;
    private String orderDescription;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private OrderStatus orderStatus; //fulfilled or unfulfilled need an enum
    private OrderType orderType; //pick up or dine-in need an enum
    private PaymentMethod orderPaymentMethod;
    private double orderPaymentAmount;
}
