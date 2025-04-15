package com.quan.dto;

import com.quan.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUser {

    private String title;

    @Column(length = 1000)
    private List<String> images;

    @Column(length = 1000)
    private String description;

    private Long id;

    private Double distance;
    private Double hours;
    private Double rating;

    private boolean open = false;
}
