package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.entity.Payment;
import com.embula.embula_backend.repository.PaymentRepository;
import com.embula.embula_backend.services.PaymentService;
import com.embula.embula_backend.util.mappers.PaymentMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private String generatePaymentId() {
        long count = paymentRepository.countAllPayments();
        return String.format("P-%03d", count + 1);
    }

}
