package com.quan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quan.dto.RestaurantDTO;
import com.quan.dto.RestaurantUser;
import com.quan.enums.ROLE_USER;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String gender;

    private String dob;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private ROLE_USER role = ROLE_USER.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer" ,orphanRemoval = true)
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shipper")
    private List<Order> shipperOrders;

    @ElementCollection
    private List<RestaurantUser> favorites;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Address> addresses;

}
