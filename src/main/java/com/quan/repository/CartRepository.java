package com.quan.repository;

import com.quan.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.customer.id = ?1")
    Cart findByUserId(Long userId);

}
