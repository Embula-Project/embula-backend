package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.response.ViewOrderDTO;

import java.util.List;

public interface OrderService {

    public String saveOrder(OrderDTO orderDTO);
    public PaginatedAllOrders viewAllOrders(int page, int size);
    public String cancelOrder(String orderId);
}
