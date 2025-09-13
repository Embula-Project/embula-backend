package com.embula.embula_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "food_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem {

    @Id
    @Column(name = "item_id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;

    @Column(name = "item_name", length = 100)
    private String itemName;

    @ElementCollection
    @CollectionTable(name = "food_item_ingredients", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "ingredient", length = 100)
    private List<String> ingredients;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "portion_size", length = 50)
    private String portionSize;

    @Column(name = "image_name", length = 100)
    private String imageName;

    @Column(name = "image_type", length = 50)
    private String imageType;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @OneToMany(mappedBy = "foodItems")
    private Set<OrderFoodItem> orderFoodItems;
}
