package com.embula.embula_backend.dto.request;

import com.embula.embula_backend.entity.enums.ComplaintType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerResponseRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    private String phone;

    @NotNull(message = "Complaint type is required")
    private ComplaintType complaintType;

    @NotBlank(message = "Description cannot be empty")
    private String description;
}
