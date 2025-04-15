package com.quan.service.impl;

import com.quan.model.Cart;
import com.quan.model.CartItem;
import com.quan.model.Food;
import com.quan.model.User;
import com.quan.repository.CartItemRepository;
import com.quan.repository.CartRepository;
import com.quan.repository.FoodRepository;
import com.quan.repository.UserRepository;
import com.quan.request.AddCartItemRequest;
import com.quan.service.CartService;
import com.quan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Cart cart = cartRepository.findByUserId(user.getId());
        Food food = foodRepository.findById(req.getFoodId()).orElseThrow(
                ()-> new Exception("not found food")
        );
        for(CartItem cartItem : cart.getCartItems()) {
            if(cartItem.getFood().getId().equals(food.getId())
                    && checkExistCartItem(req.getIngredients(), cartItem.getIngredients())) {
                int newQuantity = cartItem.getQuantity()+req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setFood(food);
        cartItem.setPrice(req.getPrice());//add price
        cartItem.setQuantity(req.getQuantity());
        cartItem.setIngredients(req.getIngredients());
        cartItem.setTotalPrice(req.getQuantity()* req.getPrice());

//        cart.getCartItems().add(cartItem);
//        cartRepository.save(cart);
        return cartItemRepository.save(cartItem);
    }

    boolean checkExistCartItem(List<String> req, List<String> cartItem){
        req.sort(String::compareTo);
        cartItem.sort(String::compareTo);
        if(req.size() != cartItem.size()) {
            return false;
        }
        for(int i = 0; i < cartItem.size(); i++) {
            if(!cartItem.get(i).equals(req.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        CartItem cartItem = findCartItemById(cartItemId);
        Cart cart = cartItem.getCart();

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity*cartItem.getPrice());
        CartItem savedCartItem =  cartItemRepository.save(cartItem);
        cart.setTotal(calculateCartTotals(cart));
        cartRepository.save(cart);
        return savedCartItem;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Cart cart = cartRepository.findByUserId(user.getId());
        CartItem cartItem = findCartItemById(cartItemId);
        cart.getCartItems().remove(cartItem);
        cart.setTotal(calculateCartTotals(cart));
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {
        Long total = 0L;
        for(CartItem cartItem : cart.getCartItems()) {
            total += cartItem.getTotalPrice();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        return cartRepository.findById(id).orElseThrow(
                () -> new Exception("not found cart")
        );
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception{
        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotal(calculateCartTotals(cart));
        return cartRepository.save(cart);
    }

    @Override
    public Cart clearCart(Long userId) throws Exception{
        Cart cart = findCartByUserId(userId);
        cart.getCartItems().clear();
        cart.setTotal(0L);
        return cartRepository.save(cart);
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws Exception {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                ()-> new Exception("not found cartItem")
        );
        return cartItem;
    }
}
