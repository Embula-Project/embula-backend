package com.embula.embula_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="order_food_item")
public class OrderFoodItem {

    @Id
    @Column(name="order_food_item_id", nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderFoodItemId;

    @Column(name="item_name", nullable=false)
    private String itemName;

    @Column(name="qty", nullable=false)
    private int qty;

    @Column(name="amount", nullable=false)
    private double amount;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order orders;

    @ManyToOne
    @JoinColumn(name="foodItem_id", nullable=false)
    private FoodItem foodItems;


}
