package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.PaymentDTO;
import com.embula.embula_backend.dto.request.OrderFoodItemRequest;
import com.embula.embula_backend.dto.request.PaymentRequest;
import com.embula.embula_backend.dto.request.RequestOrderFoodItemSaveDTO;
import com.embula.embula_backend.dto.request.RequestOrderSaveDTO;
import com.embula.embula_backend.dto.response.PaymentResponse;
import com.embula.embula_backend.entity.Payment;
import com.embula.embula_backend.entity.enums.OrderType;
import com.embula.embula_backend.services.EmailService;
import com.embula.embula_backend.services.OrderService;
import com.embula.embula_backend.services.PaymentService;
import com.embula.embula_backend.services.StripeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StripeServiceIMPL implements StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PaymentResponse checkoutProduct(PaymentRequest paymentRequest){
        Stripe.apiKey = secretKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(paymentRequest.getOrderName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(paymentRequest.getCurrency() == null ? "LKR" : paymentRequest.getCurrency())
                .setUnitAmount(paymentRequest.getAmount()*100)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(paymentRequest.getQuantity())
                .setPriceData(priceData)
                .build();

        // Build metadata with order information
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:3000/cancel")
                .addLineItem(lineItem)
                .putMetadata("customerId", paymentRequest.getCustomerId())
                .putMetadata("customerEmail", paymentRequest.getCustomerEmail())
                .putMetadata("orderName", paymentRequest.getOrderName())
                .putMetadata("orderDescription", paymentRequest.getOrderDescription() != null ? paymentRequest.getOrderDescription() : "")
                .putMetadata("orderType", paymentRequest.getOrderType() != null ? paymentRequest.getOrderType() : "DINE_IN");

        // Serialize order food items to JSON if provided
        if (paymentRequest.getOrderFoodItems() != null && !paymentRequest.getOrderFoodItems().isEmpty()) {
            try {
                String orderFoodItemsJson = objectMapper.writeValueAsString(paymentRequest.getOrderFoodItems());
                paramsBuilder.putMetadata("orderFoodItems", orderFoodItemsJson);
            } catch (JsonProcessingException e) {
                System.out.println("Error serializing order food items: " + e.getMessage());
            }
        }

        SessionCreateParams params = paramsBuilder.build();
        Session session = null;

        try{
            session = Session.create(params);
        }catch(StripeException e){
            System.out.println(e.getMessage());
        }

        if (session != null) {
            return PaymentResponse
                    .builder()
                    .status("200")
                    .message("Payment Session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } else {
            return PaymentResponse
                    .builder()
                    .status("500")
                    .message("Failed to create payment session")
                    .build();
        }
    }

    @Override
    public String handlePaymentSuccess(String sessionId) {
        Stripe.apiKey = secretKey;

        try {
            // Retrieve the session from Stripe to verify it was successful
            Session session = Session.retrieve(sessionId);

            // Check if payment was successful
            if ("paid".equals(session.getPaymentStatus())) {

                String stripePaymentIntentId = session.getPaymentIntent();

                // Check if payment already exists (prevent duplicate processing)
                Optional<Payment> existingPayment = paymentService.findByStripePaymentIntentId(stripePaymentIntentId);
                if (existingPayment.isPresent()) {
                    return "Payment " + existingPayment.get().getPaymentId() + " was already processed successfully";
                }

                // 1. Save payment to database and get the saved Payment entity
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setPaymentMethod("CARD");
                paymentDTO.setPaymentAmount(session.getAmountTotal() / 100.0); // Convert from cents
                paymentDTO.setStripePaymentIntentId(stripePaymentIntentId);
                Payment savedPayment = paymentService.savePaymentAndReturn(paymentDTO);

                // 2. Create order with the payment ID
                String customerId = session.getMetadata().get("customerId");
                String customerEmail = session.getMetadata().get("customerEmail");
                String orderName = session.getMetadata().get("orderName");
                String orderDescription = session.getMetadata().get("orderDescription");
                String orderType = session.getMetadata().get("orderType");
                String orderFoodItemsJson = session.getMetadata().get("orderFoodItems");

                // Validate customerId
                if (customerId == null || customerId.isEmpty()) {
                    throw new RuntimeException("Customer ID is missing from payment session metadata");
                }

                // Build RequestOrderSaveDTO from metadata
                RequestOrderSaveDTO orderSaveDTO = new RequestOrderSaveDTO();
                orderSaveDTO.setCustomers(customerId);
                orderSaveDTO.setOrderName(orderName);
                orderSaveDTO.setOrderDescription(orderDescription);

                // Parse and set order type
                try {
                    orderSaveDTO.setOrderType(OrderType.valueOf(orderType != null ? orderType : "DINE_IN"));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid order type: " + orderType + ", defaulting to DINE_IN");
                    orderSaveDTO.setOrderType(OrderType.DineIn);
                }

                orderSaveDTO.setPaymentId(savedPayment.getPaymentId());

                // Parse order food items from JSON if available
                List<OrderFoodItemRequest> foodItemRequests = new ArrayList<>();
                if (orderFoodItemsJson != null && !orderFoodItemsJson.isEmpty()) {
                    try {
                        foodItemRequests = objectMapper.readValue(orderFoodItemsJson, new TypeReference<List<OrderFoodItemRequest>>() {});

                        List<RequestOrderFoodItemSaveDTO> orderFoodItems = new ArrayList<>();
                        for (OrderFoodItemRequest item : foodItemRequests) {
                            RequestOrderFoodItemSaveDTO foodItemDTO = new RequestOrderFoodItemSaveDTO();
                            foodItemDTO.setItemName(item.getItemName());
                            foodItemDTO.setQty(item.getQty());
                            foodItemDTO.setAmount(item.getAmount());
                            foodItemDTO.setFoodItems(item.getFoodItemId());
                            orderFoodItems.add(foodItemDTO);
                        }
                        orderSaveDTO.setOrderFoodItem(orderFoodItems);
                        System.out.println("Parsed " + orderFoodItems.size() + " order food items");
                    } catch (JsonProcessingException e) {
                        System.out.println("Error parsing order food items: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                // 3. Save order with payment reference
                System.out.println("Now calling orderService.saveOrderWithPayment...");
                try {
                    String orderResult = orderService.saveOrderWithPayment(orderSaveDTO, savedPayment);
                    emailService.sendOrderConfirmationEmail(
                        customerEmail,
                        savedPayment.getPaymentId(),
                        savedPayment.getPaymentAmount(),
                        orderName,
                        orderDescription,
                        foodItemRequests
                    );

                    return "Payment " + savedPayment.getPaymentId() + " processed successfully. " + orderResult;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Payment " + savedPayment.getPaymentId() + " saved but order creation failed: " + e.getMessage(), e);
                }

            } else {
                throw new RuntimeException("Payment was not successful. Status: " + session.getPaymentStatus());
            }

        } catch (StripeException e) {
            throw new RuntimeException("Failed to retrieve session from Stripe: " + e.getMessage());
        }
    }
}
