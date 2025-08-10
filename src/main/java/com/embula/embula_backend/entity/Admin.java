package com.embula.embula_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admin")
public class Admin {

    @Id
    private String id;

    private String adminName;

    private String adminNumber;
}
