package com.embula.embula_backend.dto.response;

import com.embula.embula_backend.entity.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private CustomerStatus status;
    private String address;
    private String phone;
}

