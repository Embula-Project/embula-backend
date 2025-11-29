package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.PaymentRequest;
import com.embula.embula_backend.dto.response.PaymentResponse;
import com.embula.embula_backend.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.StripeResponse;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceIMPL implements StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public PaymentResponse checkoutProduct(PaymentRequest paymentRequest){
        Stripe.apiKey = secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(paymentRequest.getOrderName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(paymentRequest.getCurrency() == null ? "LKR" : paymentRequest.getCurrency())
                .setUnitAmount(paymentRequest.getAmount())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()//Line Item has all the details of the product ->Price,Quantiy, Product
                .setQuantity(paymentRequest.getQuantity())
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();
        Session session =null;

        try{
            session = Session.create(params);
        }catch(StripeException e){
            System.out.println(e.getMessage());
        }
        return PaymentResponse
                .builder()
                .status("200")
                .message("Payment Session created successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
