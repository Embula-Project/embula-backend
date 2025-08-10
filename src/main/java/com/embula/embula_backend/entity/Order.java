package com.embula.embula_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")

public class Order {

    @Id
    private String orderId;

    private String  orderName;

    private String orderDescription;

    private LocalDate orderDate;

    private LocalTime orderTime;

    private String orderStatus; //fulfilled or unfulfilled need an enum

    @CreatedDate
    private LocalDateTime orderCreatedDate;

    private String orderType; //pick up or dine-in need an enum

    private String customerId;

    private String itemId;

    private String paymentId;



}
