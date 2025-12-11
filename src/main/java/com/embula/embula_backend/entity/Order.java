package com.embula.embula_backend.entity;

import com.embula.embula_backend.entity.enums.OrderStatus;
import com.embula.embula_backend.entity.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders") // "order" is reserved in SQL
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id", length = 45)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "order_name", length = 100)
    private String orderName;

    @Column(name = "order_description", length = 500)
    private String orderDescription;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "order_time")
    private LocalTime orderTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 20)
    private OrderStatus orderStatus;

    @Column(name = "order_created_date")
    private LocalDateTime orderCreatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 20)
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    @OneToMany(mappedBy = "orders")
    private Set<OrderFoodItem> orderFoodItem;

    @OneToOne
    @JoinColumn(name="paymentId", nullable=false)
    private Payment payment;

    public Order(Customer customers, String orderName , String orderDescription, LocalDate orderDate, LocalTime orderTime,OrderStatus orderStatus,LocalDateTime orderCreatedDate, OrderType orderType) {
           this.customer = customers;
           this.orderName = orderName;
           this.orderDescription = orderDescription;
           this.orderDate=orderDate;
           this.orderTime= orderTime;
           this.orderStatus = orderStatus;
           this.orderCreatedDate = orderCreatedDate;
           this.orderType = orderType;
    }

}
