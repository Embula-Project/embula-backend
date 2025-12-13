package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.dto.request.AllPaymentDTO;
import com.embula.embula_backend.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    public String savePayment (PaymentDTO paymentDTO);

    public Payment savePaymentAndReturn(PaymentDTO paymentDTO);

    public Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);

    public List<AllPaymentDTO> getAllPayments();
}
