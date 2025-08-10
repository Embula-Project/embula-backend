package com.embula.embula_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment")
public class Payment {

    @Id
    private String paymentId;

    private String paymentMethod;

    private String paymentAmount;

    @CreatedDate
    private LocalDateTime paymentDate;

}
