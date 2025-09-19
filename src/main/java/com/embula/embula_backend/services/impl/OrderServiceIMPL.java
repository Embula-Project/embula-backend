package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.request.RequestOrderFoodItemSaveDTO;
import com.embula.embula_backend.dto.request.RequestOrderSaveDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.entity.OrderFoodItem;
import com.embula.embula_backend.entity.enums.OrderStatus;
import com.embula.embula_backend.entity.enums.OrderType;
import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.repository.CustomerRepository;
import com.embula.embula_backend.repository.FoodItemRepository;
import com.embula.embula_backend.repository.OrderFoodItemRepository;
import com.embula.embula_backend.repository.OrderRepository;
import com.embula.embula_backend.services.OrderService;
import com.embula.embula_backend.util.mappers.OrderMappers;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceIMPL implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMappers orderMappers;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FoodItemRepository foodItemRepo;

    @Autowired
    private OrderFoodItemRepository orderFoodItemRepo;

    @Override
    public String saveOrder(RequestOrderSaveDTO requestOrderSaveDTO){
        Order order= new Order(
                customerRepo.getById(requestOrderSaveDTO.getCustomers()),
                requestOrderSaveDTO.getOrderName(),
                requestOrderSaveDTO.getOrderDescription(),
                requestOrderSaveDTO.getOrderDate(),
                requestOrderSaveDTO.getOrderTime(),
                requestOrderSaveDTO.getOrderStatus(),
                requestOrderSaveDTO.getOrderCreatedDate(),
                requestOrderSaveDTO.getOrderType()
        );
        orderRepository.save(order);

        if(orderRepository.existsById(String.valueOf(order.getOrderId()))){
            List<OrderFoodItem> orderFoodItem= modelMapper.map(requestOrderSaveDTO.getOrderFoodItem(),new TypeToken<List<OrderFoodItem>>(){}.getType());
            for(int i=0;i<orderFoodItem.size();i++){
                orderFoodItem.get(i).setOrders(order);
                orderFoodItem.get(i).setFoodItems(foodItemRepo.getById(requestOrderSaveDTO.getOrderFoodItem().get(i).getFoodItems()));
            }
            if(orderFoodItem.size()>0){
                orderFoodItemRepo.saveAll(orderFoodItem);
            }
        }
        return "Successfully Saved";
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
