package com.quan.service;

import com.quan.model.Order;
import com.quan.model.User;
import com.quan.request.OrderRequest;

import java.util.List;

public interface OrderService {

    public Order createOrder(OrderRequest req, User user) throws Exception;

    public Order updateOrder(Long orderId, String orderStatus) throws Exception;

    public void cancelOrder(Long orderId) throws Exception;

    public List<Order> getUsersOrder(Long userId) throws Exception;

    public List<Order> getRestaurantsOrder(Long restautantId, String orderStatus) throws Exception;

    //shipper
    public List<Order> getShipperOrder(Long shipperId) throws Exception;

    public List<Order> getHistoryShipperOrder(Long shipperId) throws Exception;

    public Order updateOrderShipper(Long shipperId, Long orderId) throws Exception;

    public Order updateOrderSuccess(Long shipperId, Long orderId, String imgage) throws Exception;

}
