package com.quan.service.impl;

import com.quan.model.*;
import com.quan.repository.*;
import com.quan.request.OrderRequest;
import com.quan.service.CartService;
import com.quan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;


    @Override
    public Order createOrder(OrderRequest req, User user) throws Exception{

        Address address = req.getDeliveryAddress();
        addressRepository.save(address);
        if(!user.getAddresses().contains(address)) {
            user.getAddresses().add(address);
            userRepository.save(user);
        }
        Restaurant restaurant = restaurantRepository.findById(req.getRestaurantId()).get();
        Cart cart = cartService.findCartByUserId(user.getId());

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryAddress(address);
        order.setCustomer(user);
        order.setRestaurant(restaurant);
        order.setNote(req.getNote());
        order.setIsPayment(req.getIsPayment());
        order = orderRepository.save(order);

        for(CartItem cartItem: cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }
        order.setOrderStatus("PENDING");
        order.setTotalAmount(cartService.calculateCartTotals(cart));


        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order=findById(orderId);

        System.out.println("--------- "+orderStatus);

        if(orderStatus.equals("PAID") ||orderStatus.equals("DELETED") || orderStatus.equals("DELIVERING")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        else throw new Exception("Please Select A Valid Order Status");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order=findById(orderId);
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restautantId, String orderStatus) throws Exception {
        List<Order> orders=orderRepository.findByRestaurantId(restautantId);
        if(orderStatus != null){
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public List<Order> getShipperOrder(Long shipperId) throws Exception {
        List<Order> orders = orderRepository.findAll();
        User user = userRepository.findById(shipperId).get();
        orders = orders.stream().filter(order -> order.getOrderStatus().equals("PENDING")).collect(Collectors.toList());
        for(Order order: orders) {
            Address address = order.getRestaurant().getAddress();
            order.setAddressRestaurant(String.format("%s, %s, %s, %s", address.getDetailAddress(), address.getWard(),
                    address.getDistrict(), address.getProvince()));
            orderRepository.save(order);
        }
        return orders;
    }

    @Override
    public List<Order> getHistoryShipperOrder(Long shipperId) throws Exception {
        User shipper = userRepository.findById(shipperId).get();
        return shipper.getShipperOrders();
    }

    @Override
    public Order updateOrderShipper(Long shipperId, Long orderId) throws Exception {
        User shipper = userRepository.findById(shipperId).get();
        Order order=findById(orderId);
        if(order.getOrderStatus().equals("PENDING")) {
            order.setOrderStatus("DELIVERING");
            order.setShipper(shipper);
            shipper.getShipperOrders().add(order);
        }
        else{
            order.setOrderStatus("PENDING");
            shipper.getShipperOrders().remove(order);
            order.setShipper(null);
        }
        userRepository.save(shipper);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderSuccess(Long shipperId, Long orderId, String image) throws Exception {
        Order order=findById(orderId);
        order.setOrderStatus("COMPLETED");
        order.setIsPayment(true);
        order.setDeliveredAt(LocalDateTime.now());
        order.setImageDelivery(image);
        return orderRepository.save(order);
    }

    private Order findById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(
                ()->new Exception("Order Not Found")
        );
    }
}
