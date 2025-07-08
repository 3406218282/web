package com.restaurant.orderingsystem.repository;

import com.restaurant.orderingsystem.entity.CartItem;
import com.restaurant.orderingsystem.entity.MenuItem;
import com.restaurant.orderingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUser(User user);
    
    Optional<CartItem> findByUserAndMenuItem(User user, MenuItem menuItem);
    
    void deleteByUser(User user);
} 