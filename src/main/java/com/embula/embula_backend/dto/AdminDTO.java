package com.embula.embula_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String id;
    private String adminName;
    private String adminNumber;
}
