package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path="/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path="savePayment")
    public String savePayments (PaymentDTO paymentDTO) {
        String message = paymentService.savePayment(paymentDTO);
        return message;
    }

}
