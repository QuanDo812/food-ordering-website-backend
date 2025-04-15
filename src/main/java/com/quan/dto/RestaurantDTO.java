package com.quan.dto;

import com.quan.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private String title;

    @Column(length = 1000)
    private List<String> images;

    @Column(length = 1000)
    private String description;

    private Long id;

    private Double distance;
    private Double hours;
    private Double rating;
    private Address address;

    private String openingHours;

    private Long reviewCount;

    private Double lat;
    private Double lon;

    private String foods;

    private boolean open = false;
}
