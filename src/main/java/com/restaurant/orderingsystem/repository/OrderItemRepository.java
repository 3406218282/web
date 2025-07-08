package com.restaurant.orderingsystem.repository;

import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder(Order order);
} 