package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Double discountPercentage;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Boolean isActive;
}
