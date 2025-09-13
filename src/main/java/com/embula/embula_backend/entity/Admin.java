package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @Column(name = "admin_id", length = 45)
    private String id;

    @Column(name = "admin_name", length = 100)
    private String adminName;

    @Column(name = "admin_number", length = 20)
    private String adminNumber;
}
