package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.OrderDTO;
import com.embula.embula_backend.dto.paginated.PaginatedAllOrders;
import com.embula.embula_backend.dto.paginated.PaginatedStatusCustomerOrders;
import com.embula.embula_backend.dto.queryInterface.StatusCustomerOrderInterface;
import com.embula.embula_backend.dto.request.RequestOrderFoodItemSaveDTO;
import com.embula.embula_backend.dto.request.RequestOrderSaveDTO;
import com.embula.embula_backend.dto.response.StatusCustomerOrdersDTO;
import com.embula.embula_backend.dto.response.ViewOrderDTO;
import com.embula.embula_backend.entity.FoodItem;
import com.embula.embula_backend.entity.Order;
import com.embula.embula_backend.entity.OrderFoodItem;
import com.embula.embula_backend.entity.Payment;
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
import java.util.ArrayList;
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
    public String saveOrderWithPayment(RequestOrderSaveDTO requestOrderSaveDTO, Payment payment) {
        try {
            System.out.println("=== Starting saveOrderWithPayment ===");
            System.out.println("Customer ID: " + requestOrderSaveDTO.getCustomers());
            System.out.println("Payment ID: " + payment.getPaymentId());

            // Create order with payment reference
            Order order = new Order();

            // Fetch customer
            System.out.println("Fetching customer with ID: " + requestOrderSaveDTO.getCustomers());
            order.setCustomer(customerRepo.getById(requestOrderSaveDTO.getCustomers()));
            System.out.println("Customer fetched successfully");

            order.setOrderName(requestOrderSaveDTO.getOrderName());
            order.setOrderDescription(requestOrderSaveDTO.getOrderDescription());
            order.setOrderDate(LocalDate.now()); // Current date
            order.setOrderTime(LocalTime.now()); // Current time
            order.setOrderStatus(OrderStatus.Pending); // Default status for new order
            order.setOrderCreatedDate(LocalDateTime.now()); // Current datetime
            order.setOrderType(requestOrderSaveDTO.getOrderType());
            order.setPayment(payment); // Link the payment

            System.out.println("Saving order to database...");
            orderRepository.save(order);
            System.out.println("Order saved with ID: " + order.getOrderId());

            // Save order food items if provided
            if (requestOrderSaveDTO.getOrderFoodItem() != null && !requestOrderSaveDTO.getOrderFoodItem().isEmpty()) {
                System.out.println("Saving " + requestOrderSaveDTO.getOrderFoodItem().size() + " order food items...");
                List<OrderFoodItem> orderFoodItems = new ArrayList<>();
                for (RequestOrderFoodItemSaveDTO foodItemDTO : requestOrderSaveDTO.getOrderFoodItem()) {
                    OrderFoodItem orderFoodItem = new OrderFoodItem();
                    orderFoodItem.setOrders(order);
                    orderFoodItem.setItemName(foodItemDTO.getItemName());
                    orderFoodItem.setQty(foodItemDTO.getQty());
                    orderFoodItem.setAmount(foodItemDTO.getAmount());

                    System.out.println("Fetching food item with ID: " + foodItemDTO.getFoodItems());
                    orderFoodItem.setFoodItems(foodItemRepo.getById(foodItemDTO.getFoodItems()));
                    orderFoodItems.add(orderFoodItem);
                }
                orderFoodItemRepo.saveAll(orderFoodItems);
                System.out.println("Order food items saved successfully");
            } else {
                System.out.println("No order food items to save");
            }

            String result = "Order " + order.getOrderId() + " saved successfully with Payment " + payment.getPaymentId();
            System.out.println("=== " + result + " ===");
            return result;
        } catch (Exception e) {
            System.err.println("=== ERROR in saveOrderWithPayment ===");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Failed to save order: " + e.getMessage(), e);
        }
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

    @Override
    public PaginatedStatusCustomerOrders statusCustomerOrders (String status, int page , int size){
        List<StatusCustomerOrderInterface> statusCustomerOrderInterfaces = orderRepository.statusCustomerOrders(status, PageRequest.of(page,size));
        List<StatusCustomerOrdersDTO> statusCustomerOrdersDTO = new ArrayList<>();
        statusCustomerOrdersDTO = orderMappers.statusCustomerOrderInterfacesToStatusCustomerOrdersDTO(statusCustomerOrderInterfaces);
        PaginatedStatusCustomerOrders paginatedStatusCustomerOrders = new PaginatedStatusCustomerOrders(
                statusCustomerOrdersDTO,
                orderRepository.countStatusCustomerOrders(status)
        );

        return paginatedStatusCustomerOrders;
    }
}
