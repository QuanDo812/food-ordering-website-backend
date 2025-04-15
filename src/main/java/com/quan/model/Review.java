package com.quan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comment;

    private Long rating;

    @JsonIgnore
    @ManyToOne
    private User customer;

    private String nameCustomer;

    private LocalDateTime reviewDate;

    @JsonIgnore
    @ManyToOne
    private Food food;

}
