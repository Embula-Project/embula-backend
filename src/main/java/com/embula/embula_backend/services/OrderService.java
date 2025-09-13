package com.embula.embula_backend.services;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.request.RequestOrderSaveDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;

import java.util.List;

public interface OrderService {

    public String saveOrder(RequestOrderSaveDTO requestOrderSaveDTO);
    public PaginatedAllOrders viewAllOrders(int page, int size);
    public String cancelOrder(String orderId);
}
