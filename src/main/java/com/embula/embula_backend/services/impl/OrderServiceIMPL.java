package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.entity.enums.OrderStatus;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.repository.OrderRepository;
import com.embula.embula_backend.services.OrderService;
import com.embula.embula_backend.util.mappers.OrderMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceIMPL implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMappers orderMappers;

    @Override
    public String saveOrder(OrderDTO orderDTO) {
        Order order = orderMappers.OrderDTOToOrder(orderDTO);
        orderRepository.save(order);
        return order.getOrderId()+ " Order Successfully Saved";
    }

    @Override
    public PaginatedAllOrders viewAllOrders(int page, int size){
        Page<Order> order= orderRepository.findAll(PageRequest.of(page,size));
        long count = order.getTotalElements();
        if(!order.isEmpty()){
            PaginatedAllOrders paginatedAllOrders = new PaginatedAllOrders(
                    orderMappers.OrderToViewOrderDTO(order),count
            );
            return paginatedAllOrders;
        }else{
            throw new NotFoundException("No Orders Found");
        }
    }

    @Override
    public String cancelOrder(String orderId){
        if(orderRepository.existsById(orderId)){
            Order order= orderRepository.findById(orderId).get();
            order.setOrderStatus(OrderStatus.Cancelled);
            orderRepository.save(order);
            return order.getOrderId()+ " Order Successfully cancelled";
        }else{
            throw new NotFoundException("No Order Found");
        }


    }
}
