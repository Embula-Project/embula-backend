package com.embula.embula_backend.dto.request;

import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllPaymentDTO {
    private PaymentMethod paymentMethod;
    private double paymentAmount;
    private LocalDateTime paymentDate;
}
