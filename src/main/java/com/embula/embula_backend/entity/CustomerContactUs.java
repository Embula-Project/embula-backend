package com.embula.embula_backend.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer_contact_us")
public class CustomerContactUs {

    @Id
    private String inquiryId;

    private String email;

    private String description;

    private String phone;

    private String customerId;


}
