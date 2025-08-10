package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.entity.Payment;

public interface PaymentService {

    public String savePayment (PaymentDTO paymentDTO);

}
