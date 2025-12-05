package com.embula.embula_backend.controller;

import com.embula.embula_backend.dto.request.PaymentRequest;
import com.embula.embula_backend.dto.response.PaymentResponse;
import com.embula.embula_backend.services.StripeService;
import com.embula.embula_backend.util.StandardResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food-order")
public class CheckoutController {

    private StripeService stripeService;

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse> checkoutProduct(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = stripeService.checkoutProduct(paymentRequest);
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(200,"Success", paymentResponse),
                HttpStatus.CREATED
        );
        return responseEntity;
    }

    @GetMapping("/payment-success")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse> paymentSuccess(@RequestParam("session_id") String sessionId) {
        try {
            String message = stripeService.handlePaymentSuccess(sessionId);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Success", message),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(500, "Error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
