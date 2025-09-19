package com.embula.embula_backend.controller;


import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.services.PaymentService;
import com.embula.embula_backend.util.StandardResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ROLE_ADMIN")
    public ResponseEntity<StandardResponse> savePayments (PaymentDTO paymentDTO) {
        String message = paymentService.savePayment(paymentDTO);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", message),
                HttpStatus.OK
        );
        return responseEntity;
    }

}
