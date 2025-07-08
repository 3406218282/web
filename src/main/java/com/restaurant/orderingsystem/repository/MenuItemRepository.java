package com.restaurant.orderingsystem.repository;

import com.restaurant.orderingsystem.entity.Category;
import com.restaurant.orderingsystem.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByCategory(Category category);
    
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    List<MenuItem> findByAvailability(MenuItem.Availability availability);
} 