package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "restaurant_tables")
@Data
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tableNumber;

    @Column(nullable = false)
    private int capacity;

    private String location; // e.g., "Patio", "Indoors", "Window"

    @Column(nullable = false)
    private boolean isActive = true;
}
