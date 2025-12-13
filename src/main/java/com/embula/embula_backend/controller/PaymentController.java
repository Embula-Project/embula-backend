package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.dto.request.AllPaymentDTO;
import com.embula.embula_backend.services.PaymentService;
import com.embula.embula_backend.util.StandardResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path="/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path="savePayment")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> savePayments (PaymentDTO paymentDTO) {
        String message = paymentService.savePayment(paymentDTO);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getAllPayments(){
        List<AllPaymentDTO> allPaymentDTO = paymentService.getAllPayments();
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", allPaymentDTO),
                HttpStatus.OK
        );
        return responseEntity;
    }
}
