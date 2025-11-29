package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.PaymentRequest;
import com.embula.embula_backend.dto.response.PaymentResponse;

public interface StripeService {

    public PaymentResponse checkoutProduct(PaymentRequest paymentRequest);
}
