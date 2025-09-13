package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_contact_us")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContactUs {

    @Id
    @Column(name = "inquiry_id", length = 45)
    private String inquiryId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "customer_id", length = 45)
    private String customerId;
}
