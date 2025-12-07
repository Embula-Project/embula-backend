package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.entity.Payment;
import com.embula.embula_backend.entity.enums.PaymentMethod;
import com.embula.embula_backend.repository.PaymentRepository;
import com.embula.embula_backend.services.PaymentService;
import com.embula.embula_backend.util.mappers.PaymentMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceIMPL implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private PaymentMappers paymentMappers;


    @Override
    public String savePayment (PaymentDTO paymentDTO){
        // Generate payment ID if not provided
        if (paymentDTO.getPaymentId() == null || paymentDTO.getPaymentId().isEmpty()) {
            paymentDTO.setPaymentId(generatePaymentId());
        }

        Payment payment = paymentMappers.savePayments(paymentDTO);
        paymentRepository.save(payment);
        return paymentDTO.getPaymentId() + " Saved Successfully";
    }

    @Override
    public Payment savePaymentAndReturn(PaymentDTO paymentDTO) {
        // Generate payment ID if not provided
        if (paymentDTO.getPaymentId() == null || paymentDTO.getPaymentId().isEmpty()) {
            paymentDTO.setPaymentId(generatePaymentId());
        }

        Payment payment = new Payment();
        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        payment.setPaymentAmount(paymentDTO.getPaymentAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStripePaymentIntentId(paymentDTO.getStripePaymentIntentId());

        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return paymentRepository.findByStripePaymentIntentId(stripePaymentIntentId);
    }

    private String generatePaymentId() {
        long count = paymentRepository.countAllPayments();
        return String.format("P-%03d", count + 1);
    }

}
