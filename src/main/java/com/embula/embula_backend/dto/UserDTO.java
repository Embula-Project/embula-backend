package com.embula.embula_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String role;

    // Customer specific fields (optional - only needed when role is CUSTOMER)
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private byte[] image;

    // Admin specific fields (optional - only needed when role is ADMIN)
    private String adminName;
    private String adminNumber;
}
