package com.restaurant.orderingsystem.service.impl;

import com.restaurant.orderingsystem.entity.*;
import com.restaurant.orderingsystem.repository.CartItemRepository;
import com.restaurant.orderingsystem.repository.OrderItemRepository;
import com.restaurant.orderingsystem.repository.OrderRepository;
import com.restaurant.orderingsystem.repository.UserRepository;
import com.restaurant.orderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 从购物车创建订单
     *
     * @param userId 用户ID
     * @return 创建的订单
     */
    @Override
    @Transactional
    public Order createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));

        // 获取用户购物车项
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法创建订单");
        }

        // 创建订单
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        // 计算订单总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = cartItem.getMenuItem();
            
            // 检查菜品是否可用
            if (menuItem.getAvailability() != MenuItem.Availability.AVAILABLE) {
                throw new RuntimeException("菜品目前不可用: " + menuItem.getName());
            }

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(menuItem.getPrice());

            orderItems.add(orderItem);

            // 累加总金额
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        order.setTotalPrice(totalPrice);
        
        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 保存订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        // 清空购物车
        cartItemRepository.deleteByUser(user);

        return savedOrder;
    }

    /**
     * 根据ID获取订单
     *
     * @param orderId 订单ID
     * @return 订单
     */
    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * 获取用户的所有订单
     *
     * @param user 用户
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    /**
     * 根据用户ID获取订单
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        return getOrdersByUser(user);
    }

    /**
     * 获取指定状态的订单
     *
     * @param status 订单状态
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * 获取时间范围内的订单
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByCreatedAtBetween(start, end);
    }

    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status  新状态
     * @return 更新后的订单
     */
    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在，ID: " + orderId));

        // 检查是否可以更新状态
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("订单已取消，无法更新状态");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw new RuntimeException("订单已完成，无法更新状态");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @return 取消后的订单
     */
    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在，ID: " + orderId));

        // 检查是否可以取消
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("订单已经取消");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw new RuntimeException("订单已完成，无法取消");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    /**
     * 获取所有订单
     *
     * @return 订单列表
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
} 