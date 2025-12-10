package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.OrderFoodItemRequest;
import java.util.List;

public interface EmailService {

    public String sendEmail(String to, String subject, String content);

    public String sendOrderConfirmationEmail(
            String customerEmail,
            String paymentId,
            double totalAmount,
            String orderName,
            String orderDescription,
            List<OrderFoodItemRequest> orderItems
    );
}
