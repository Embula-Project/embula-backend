package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "dine_in_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DineInOrder {

    @Id
    @Column(name = "dine_in_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dineInOrderId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservation_time", nullable = false)
    private LocalTime reservationTime;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
