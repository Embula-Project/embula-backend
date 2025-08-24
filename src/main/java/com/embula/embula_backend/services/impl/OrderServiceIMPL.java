package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.repository.OrderRepository;
import com.embula.embula_backend.services.OrderService;
import com.embula.embula_backend.util.mappers.OrderMappers;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ViewOrderDTO> viewAllOrders(){
        List<Order> order = orderRepository.findAll();
        if(!order.isEmpty()){
            List<ViewOrderDTO> viewOrderDTO = orderMappers.OrderToViewOrderDTO(order);
            return viewOrderDTO;
        }else{
            throw new NotFoundException("No Orders Found");
        }

    }
}
