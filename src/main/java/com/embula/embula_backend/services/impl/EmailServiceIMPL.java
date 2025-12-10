package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.OrderFoodItemRequest;
import com.embula.embula_backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceIMPL implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.admin-mail}")
    private String fromEmail;

    @Override
    public String sendEmail(String to, String subject, String body) {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        return "EmailSent";
    }

    @Override
    public String sendOrderConfirmationEmail(String customerEmail, String paymentId, double totalAmount, String orderName, String orderDescription, List<OrderFoodItemRequest> orderItems) {
        String subject = "Order Confirmed - Payment Successful";
        String body = buildOrderConfirmationEmailBody(
                customerEmail,
                paymentId,
                totalAmount,
                orderName,
                orderDescription,
                orderItems
        );
        return sendEmail(customerEmail, subject, body);
    }


    private String buildOrderConfirmationEmailBody(
            String customerEmail,
            String paymentId,
            double totalAmount,
            String orderName,
            String orderDescription,
            List<OrderFoodItemRequest> orderItems) {

        StringBuilder emailBody = new StringBuilder();

        emailBody.append("Dear Valued Customer,\n\n");
        emailBody.append("Thank you for your order! We're pleased to confirm that your payment has been successfully processed.\n\n");
        emailBody.append("═══════════════════════════════════════════\n");
        emailBody.append("ORDER CONFIRMATION\n");
        emailBody.append("═══════════════════════════════════════════\n\n");

        // Payment Details Section
        emailBody.append("PAYMENT DETAILS:\n");
        emailBody.append("─────────────────────────────────────────\n");
        emailBody.append(String.format("Payment ID:        %s\n", paymentId));
        emailBody.append(String.format("Total Amount:      LKR %.2f\n", totalAmount));
        emailBody.append(String.format("Payment Method:    Credit/Debit Card\n"));
        emailBody.append(String.format("Customer Email:    %s\n\n", customerEmail));

        // Order Details Section
        emailBody.append("ORDER DETAILS:\n");
        emailBody.append("─────────────────────────────────────────\n");
        emailBody.append(String.format("Order Name:        %s\n", orderName != null ? orderName : "N/A"));

        if (orderDescription != null && !orderDescription.isEmpty()) {
            emailBody.append(String.format("Description:       %s\n", orderDescription));
        }

        // Order Items Section
        if (orderItems != null && !orderItems.isEmpty()) {
            emailBody.append("\nORDER ITEMS:\n");
            emailBody.append("─────────────────────────────────────────\n");

            double itemsTotal = 0.0;
            for (OrderFoodItemRequest item : orderItems) {
                double itemTotal = item.getAmount() * item.getQty();
                itemsTotal += itemTotal;
                emailBody.append(String.format("• %s\n", item.getItemName()));
                emailBody.append(String.format("  Quantity: %d × LKR %.2f = LKR %.2f\n\n",
                    item.getQty(), item.getAmount(), itemTotal));
            }

            emailBody.append("─────────────────────────────────────────\n");
            emailBody.append(String.format("Subtotal:          LKR %.2f\n", itemsTotal));
            emailBody.append(String.format("Total Paid:        LKR %.2f\n", totalAmount));
        }

        emailBody.append("\n═══════════════════════════════════════════\n\n");

        // Footer Section
        emailBody.append("WHAT'S NEXT?\n");
        emailBody.append("─────────────────────────────────────────\n");
        emailBody.append("Your order is being prepared and will be ready shortly.\n");
        emailBody.append("You will receive updates on your order status via email.\n\n");

        emailBody.append("Need Help?\n");
        emailBody.append("If you have any questions or concerns about your order,\n");
        emailBody.append("please don't hesitate to contact our support team.\n\n");

        emailBody.append("Thank you for choosing Embula Restaurant!\n");
        emailBody.append("We look forward to serving you again.\n\n");

        emailBody.append("Best regards,\n");
        emailBody.append("Embula Restaurant Team\n\n");

        emailBody.append("─────────────────────────────────────────\n");
        emailBody.append("This is an automated message. Please do not reply to this email.\n");
        emailBody.append("For support inquiries, please contact: support@embula.com\n");

        return emailBody.toString();
    }
}
