package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "take_away_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAwayOrder {

    @Id
    @Column(name = "take_away_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long takeAwayOrderId;

    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "pickup_time", nullable = false)
    private LocalTime pickupTime;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
