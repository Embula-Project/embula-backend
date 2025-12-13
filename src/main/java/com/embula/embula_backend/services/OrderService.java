package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.paginated.PaginatedStatusCustomerOrders;
import com.embula.embula_backend.dto.request.RequestOrderSaveDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.Payment;
import com.embula.embula_backend.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    public String saveOrder(RequestOrderSaveDTO requestOrderSaveDTO);

    public String saveOrderWithPayment(RequestOrderSaveDTO requestOrderSaveDTO, Payment payment);

    public PaginatedAllOrders viewAllOrders(int page, int size);
    public String updateOrderStatus(Long orderId, OrderStatus orderStatus);
    public PaginatedStatusCustomerOrders statusCustomerOrders(String status, int page , int size);
}
