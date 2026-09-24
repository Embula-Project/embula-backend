package com.embula.embula_backend.entity;

import com.embula.embula_backend.entity.enums.ComplaintType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    @Id
    @Column(name = "inquiry_id", length = 45)
    private String inquiryId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_type", length = 20)
    private ComplaintType complaintType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
