package com.quan.controller;

import com.quan.model.Order;
import com.quan.model.User;
import com.quan.request.CompletingRequest;
import com.quan.service.OrderService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipper")
public class ShipperOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllShipperOrders(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        List<Order> orders = orderService.getShipperOrder(user.getId());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/history")
    public ResponseEntity<List<Order>> getHistoryShipperOrders(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        List<Order> orders = orderService.getHistoryShipperOrder(user.getId());

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/delivering/{orderId}")
    public ResponseEntity<Order> updateShipperOrderStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.updateOrderShipper(user.getId(), orderId);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/success/{orderId}")
    public ResponseEntity<Order> updateOrderSuccess(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CompletingRequest req,
            @PathVariable Long orderId
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.updateOrderSuccess(user.getId(), orderId, req.getImage());
        return ResponseEntity.ok(order);
    }

}
