package com.embula.embula_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String address;
    private List<String> phone;
    private Binary image;
    private LocalDateTime createdAt;


}
