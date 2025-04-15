package com.quan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;

    private Long totalPrice;

    @Column(length = 1000)
    private List<String> ingredients;

    @ManyToOne
    @JoinColumn(name ="food_id")
    private Food food;

    @JsonIgnore
    @ManyToOne
    private Order order;

}