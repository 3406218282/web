package com.restaurant.orderingsystem.service;

import com.restaurant.orderingsystem.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    User registerUser(User user);
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    List<User> getAllUsers();
    
    List<User> getUsersByRole(User.Role role);
    
    User updateUser(Long id, User userDetails);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
} 