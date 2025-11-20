package com.embula.embula_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusCustomerOrdersDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String orderName;

    private LocalDateTime orderCreatedDate;


}
