package com.restaurant.orderingsystem.service;

import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务接口
 * 提供订单创建和管理功能
 */
public interface OrderService {
    
    /**
     * 从购物车创建订单
     * @param userId 用户ID
     * @return 创建的订单
     */
    Order createOrderFromCart(Long userId);
    
    /**
     * 根据ID获取订单
     * @param orderId 订单ID
     * @return 订单
     */
    Optional<Order> getOrderById(Long orderId);
    
    /**
     * 获取用户的所有订单
     * @param user 用户
     * @return 订单列表
     */
    List<Order> getOrdersByUser(User user);
    
    /**
     * 根据用户ID获取订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getOrdersByUserId(Long userId);
    
    /**
     * 获取指定状态的订单
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> getOrdersByStatus(Order.OrderStatus status);
    
    /**
     * 获取时间范围内的订单
     * @param start 开始时间
     * @param end 结束时间
     * @return 订单列表
     */
    List<Order> getOrdersBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 更新后的订单
     */
    Order updateOrderStatus(Long orderId, Order.OrderStatus status);
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 取消后的订单
     */
    Order cancelOrder(Long orderId);
    
    /**
     * 获取所有订单
     * @return 订单列表
     */
    List<Order> getAllOrders();
} 