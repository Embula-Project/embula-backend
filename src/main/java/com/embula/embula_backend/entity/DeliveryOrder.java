package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrder {

    @Id
    @Column(name = "delivery_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryOrderId;

    @Column(name = "delivery_address", length = 255, nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_phone", length = 20, nullable = false)
    private String deliveryPhone;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
}
