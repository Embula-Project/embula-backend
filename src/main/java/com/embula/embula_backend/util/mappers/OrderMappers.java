package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedStatusCustomerOrders;
import com.embula.embula_backend.dto.queryInterface.StatusCustomerOrderInterface;
import com.embula.embula_backend.dto.response.StatusCustomerOrdersDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMappers {
    Order OrderDTOToOrder(OrderDTO orderDTO);

    @Mapping(source = "payment.paymentMethod", target = "orderPaymentMethod")
    @Mapping(source = "payment.paymentAmount", target = "orderPaymentAmount")
    ViewOrderDTO orderToViewOrderDTO(Order order);
    List<ViewOrderDTO> OrderToViewOrderDTO(List<Order> orders);
    List<StatusCustomerOrdersDTO> statusCustomerOrderInterfacesToStatusCustomerOrdersDTO (List<StatusCustomerOrderInterface> statusCustomerOrderInterfaces);
}
