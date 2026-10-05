package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.request.CustomerResponseRequestDTO;
import com.embula.embula_backend.dto.request.OrderFoodItemRequest;
import com.embula.embula_backend.entity.Order;
import java.util.List;

public interface EmailService {

    public String sendEmail(String to, String subject, String content);

    /**
     * Sends the order confirmation email. Call only after the Order (and its
     * order-type-specific row — DineInOrder/TakeAwayOrder/DeliveryOrder) and
     * Payment have already been persisted, so the email reflects what was
     * actually saved.
     */
    public String sendOrderConfirmationEmail(
            Order order,
            String customerEmail,
            String paymentId,
            double totalAmount,
            String orderDescription,
            List<OrderFoodItemRequest> orderItems
    );

    public String sendCustomerResponseNotificationEmail(CustomerResponseRequestDTO customerResponseRequestDTO);
}
