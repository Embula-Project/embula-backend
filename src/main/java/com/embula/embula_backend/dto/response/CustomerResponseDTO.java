package com.embula.embula_backend.dto.response;

import com.embula.embula_backend.entity.enums.ComplaintType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private String inquiryId;
    private String name;
    private String email;
    private ComplaintType complaintType;
    private String description;
    private LocalDateTime createdAt;
}
