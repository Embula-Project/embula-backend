package com.embula.embula_backend.entity;

import com.embula.embula_backend.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id", length = 45)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 50)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_amount")
    private double paymentAmount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name="stripe_payment_intent_id", length = 100)
    private String stripePaymentIntentId;

    @Column(name="customer_id", nullable = false)
    private String customer;
}
