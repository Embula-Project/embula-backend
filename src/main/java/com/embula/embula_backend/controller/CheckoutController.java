package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.PaymentRequest;
import com.embula.embula_backend.dto.response.PaymentResponse;
import com.embula.embula_backend.services.StripeService;
import com.embula.embula_backend.util.StandardResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food-order")
public class CheckoutController {

    private StripeService stripeService;

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StandardResponse> checkoutProduct(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = stripeService.checkoutProduct(paymentRequest);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", paymentResponse),
                HttpStatus.CREATED
        );
        return responseEntity;
    }
}
