package com.restaurant.orderingsystem.repository;

import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<Order> findByUserAndStatus(User user, Order.OrderStatus status);
} 