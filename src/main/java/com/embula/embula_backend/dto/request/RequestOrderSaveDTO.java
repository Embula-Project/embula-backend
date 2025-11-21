package com.embula.embula_backend.dto.request;


import com.embula.embula_backend.entity.Customer;
import com.embula.embula_backend.entity.enums.OrderStatus;
import com.embula.embula_backend.entity.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class RequestOrderSaveDTO {

    private String orderName;
    private String orderDescription;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private OrderStatus orderStatus;
    private LocalDateTime orderCreatedDate;
    private OrderType orderType;
    private String customers;
    private List<RequestOrderFoodItemSaveDTO> orderFoodItem;
}
