package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMappers {
    Order OrderDTOToOrder(OrderDTO orderDTO);
    List<ViewOrderDTO> OrderToViewOrderDTO(List<Order> orders);
}
