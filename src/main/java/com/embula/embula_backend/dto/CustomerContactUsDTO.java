package com.embula.embula_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContactUsDTO {

    private String inquiryId;
    private String email;
    private String description;
    private String phone;
    private String customerId;
}
