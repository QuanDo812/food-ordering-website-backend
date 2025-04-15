package com.quan.request;

import com.quan.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Long restaurantId;

    private Address deliveryAddress;

    private String note;

    private Boolean isPayment;

}
