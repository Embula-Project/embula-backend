package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.request.CustomerResponseRequestDTO;
import com.embula.embula_backend.dto.request.OrderFoodItemRequest;
import com.embula.embula_backend.entity.DeliveryOrder;
import com.embula.embula_backend.entity.DineInOrder;
import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.entity.TakeAwayOrder;
import com.embula.embula_backend.entity.enums.OrderType;
import com.embula.embula_backend.repository.DeliveryOrderRepository;
import com.embula.embula_backend.repository.DineInOrderRepository;
import com.embula.embula_backend.repository.TakeAwayOrderRepository;
import com.embula.embula_backend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceIMPL implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final DineInOrderRepository dineInOrderRepository;
    private final TakeAwayOrderRepository takeAwayOrderRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;

    @Value("${spring.mail.admin-mail}")
    private String fromEmail;

    @Value("${spring.mail.recieve-mail}")
    private String toEmail;

    @Override
    public String sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        return "EmailSent";
    }

    /**
     * Renders a Thymeleaf template from src/main/resources/templates/ and sends it as an HTML email.
     */
    private String sendHtmlEmail(String to, String subject, String templateName, Context context) {
        String htmlBody = templateEngine.process(templateName, context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);
            return "EmailSent";
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    @Override
    public String sendCustomerResponseNotificationEmail(CustomerResponseRequestDTO customerResponseRequestDTO) {
        String complaintTypeLabel = toDisplayLabel(customerResponseRequestDTO.getComplaintType().name());
        String subject = "New " + complaintTypeLabel + " - Contact Us";

        Context context = new Context();
        context.setVariable("name", customerResponseRequestDTO.getName());
        context.setVariable("email", customerResponseRequestDTO.getEmail());
        context.setVariable("phone", customerResponseRequestDTO.getPhone());
        context.setVariable("complaintTypeLabel", complaintTypeLabel);
        context.setVariable("description", customerResponseRequestDTO.getDescription());

        return sendHtmlEmail(toEmail, subject, "customer-response-notification-email", context);
    }

    @Override
    public String sendOrderConfirmationEmail(
            Order order,
            String customerEmail,
            String paymentId,
            double totalAmount,
            String orderDescription,
            List<OrderFoodItemRequest> orderItems) {

        String subject = "Order Confirmed - Payment Successful";

        Context context = new Context();
        context.setVariable("orderId", order.getOrderId());
        context.setVariable("orderTypeLabel", toDisplayLabel(order.getOrderType().name()));
        context.setVariable("orderDescription", orderDescription);
        context.setVariable("paymentId", paymentId);
        context.setVariable("customerEmail", customerEmail);
        context.setVariable("orderItems", orderItems);
        context.setVariable("totalAmount", totalAmount);
        context.setVariable("nextStepsMessage", nextStepsMessageFor(order.getOrderType()));

        applyOrderTypeDetails(order, context);

        return sendHtmlEmail(customerEmail, subject, "order-confirmation-email", context);
    }

    /**
     * Looks up the order-type-specific row (DineInOrder/TakeAwayOrder/DeliveryOrder)
     * for the given order and exposes its details to the email template.
     */
    private void applyOrderTypeDetails(Order order, Context context) {
        if (order.getOrderType() == OrderType.DineIn) {
            dineInOrderRepository.findByOrder_OrderId(order.getOrderId()).ifPresent(dineIn -> {
                context.setVariable("scheduleLabel", "Reservation");
                context.setVariable("scheduledDate", dineIn.getReservationDate());
                context.setVariable("scheduledTime", dineIn.getReservationTime());
            });
        } else if (order.getOrderType() == OrderType.TakeAway) {
            takeAwayOrderRepository.findByOrder_OrderId(order.getOrderId()).ifPresent(takeAway -> {
                context.setVariable("scheduleLabel", "Pickup");
                context.setVariable("scheduledDate", takeAway.getPickupDate());
                context.setVariable("scheduledTime", takeAway.getPickupTime());
            });
        } else if (order.getOrderType() == OrderType.Delivery) {
            deliveryOrderRepository.findByOrder_OrderId(order.getOrderId()).ifPresent(delivery -> {
                context.setVariable("deliveryAddress", delivery.getDeliveryAddress());
                context.setVariable("deliveryPhone", delivery.getDeliveryPhone());
            });
        }
    }

    private String nextStepsMessageFor(OrderType orderType) {
        return switch (orderType) {
            case DineIn -> "Your table reservation is confirmed. We look forward to welcoming you.";
            case TakeAway -> "Your order is being prepared and will be ready for pickup at your selected time.";
            case Delivery -> "Your order is being prepared and will be delivered to your address.";
        };
    }

    private String toDisplayLabel(String enumName) {
        // Splits PascalCase / UPPER_SNAKE_CASE enum names into a readable label, e.g. "DineIn" -> "Dine In".
        String spaced = enumName.replaceAll("_", " ").replaceAll("([a-z])([A-Z])", "$1 $2");
        String[] words = spaced.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            if (!result.isEmpty()) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }
}
