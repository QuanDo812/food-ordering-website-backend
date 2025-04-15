package com.quan.controller;

import com.quan.model.Order;
import com.quan.model.User;
import com.quan.request.OrderRequest;
import com.quan.response.PaymentResponse;
import com.quan.service.CartService;
import com.quan.service.OrderService;
import com.quan.service.UserService;
import com.quan.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private VNPAYService vnpayService;

    @Autowired
    private CartService cartService;

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest order,
                                                       @RequestHeader("Authorization") String jwt,
                                                       HttpServletRequest request)
            throws Exception{
        User user=userService.findUserByJwt(jwt);
        if(order!=null) {
            Order res = orderService.createOrder(order,user);
            PaymentResponse paymentResponse = new PaymentResponse();
            if(order.getIsPayment().equals(Boolean.TRUE)) {
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String paymentUrl = vnpayService.createOrder(request, res.getTotalAmount().intValue(), res.getId(), "Do Dam Quan", baseUrl);
                cartService.clearCart(user.getId());
                paymentResponse.setPaymentUrl(paymentUrl);

            }
            else{
                paymentResponse.setPaymentUrl("");
            }
            return new ResponseEntity<>(paymentResponse,HttpStatus.CREATED);


        }else throw new Exception("Please provide valid request body");

    }



    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrdersHistory(@RequestHeader("Authorization") String jwt) throws Exception{

        User user=userService.findUserByJwt(jwt);

        List<Order> orders = orderService.getUsersOrder(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }





}
