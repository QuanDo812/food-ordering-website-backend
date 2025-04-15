package com.quan.controller;

import com.quan.model.Order;
import com.quan.response.MessageResponse;
import com.quan.service.OrderService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable Long orderId) throws Exception{
        orderService.cancelOrder(orderId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("order canceled!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    @GetMapping("/order/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getAllRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String order_status) throws Exception{

        List<Order> orders = orderService.getRestaurantsOrder(restaurantId,order_status);

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrders(@PathVariable Long orderId,@PathVariable String orderStatus) throws Exception{

        Order orders = orderService.updateOrder(orderId, orderStatus);
        return ResponseEntity.ok(orders);

    }

}
