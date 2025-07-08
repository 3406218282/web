package com.restaurant.orderingsystem.controller;

import com.restaurant.orderingsystem.dto.OrderResponse;
import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.security.UserDetailsImpl;
import com.restaurant.orderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单控制器
 * 提供订单管理的REST API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 从购物车创建新订单（仅普通用户）
     *
     * @param userDetails 当前登录用户
     * @return 创建的订单响应
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Order order = orderService.createOrderFromCart(userDetails.getId());
        OrderResponse orderResponse = OrderResponse.fromEntity(order);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    /**
     * 获取当前用户的所有订单（仅普通用户）
     *
     * @param userDetails 当前登录用户
     * @return 订单列表
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Order> orders = orderService.getOrdersByUserId(userDetails.getId());
        List<OrderResponse> orderResponses = OrderResponse.fromEntities(orders);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    /**
     * 获取订单详情（用户只能查看自己的订单，管理员和厨师可以查看所有订单）
     *
     * @param orderId     订单ID
     * @param userDetails 当前登录用户
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'CHEF', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getOrderById(orderId)
                .map(order -> {
                    // 用户只能查看自己的订单
                    if (userDetails.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
                            && !order.getUser().getId().equals(userDetails.getId())) {
                        return new ResponseEntity<OrderResponse>(HttpStatus.FORBIDDEN);
                    }
                    return new ResponseEntity<>(OrderResponse.fromEntity(order), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 取消订单（仅用户和管理员）
     *
     * @param orderId     订单ID
     * @param userDetails 当前登录用户
     * @return 取消后的订单
     */
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 检查订单是否存在
        return orderService.getOrderById(orderId)
                .map(order -> {
                    // 用户只能取消自己的订单
                    if (userDetails.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
                            && !order.getUser().getId().equals(userDetails.getId())) {
                        return new ResponseEntity<OrderResponse>(HttpStatus.FORBIDDEN);
                    }
                    
                    Order canceledOrder = orderService.cancelOrder(orderId);
                    return new ResponseEntity<>(OrderResponse.fromEntity(canceledOrder), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 更新订单状态（仅管理员和厨师）
     *
     * @param orderId 订单ID
     * @param status  新状态
     * @return 更新后的订单
     */
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('CHEF', 'ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("status") Order.OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return new ResponseEntity<>(OrderResponse.fromEntity(updatedOrder), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取所有待处理的订单（仅厨师和管理员）
     *
     * @return 待处理订单列表
     */
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('CHEF', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getPendingOrders() {
        List<Order> pendingOrders = orderService.getOrdersByStatus(Order.OrderStatus.PENDING);
        List<OrderResponse> orderResponses = OrderResponse.fromEntities(pendingOrders);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    /**
     * 获取处理中的订单（仅厨师和管理员）
     *
     * @return 处理中订单列表
     */
    @GetMapping("/processing")
    @PreAuthorize("hasAnyRole('CHEF', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getProcessingOrders() {
        List<Order> processingOrders = orderService.getOrdersByStatus(Order.OrderStatus.PROCESSING);
        List<OrderResponse> orderResponses = OrderResponse.fromEntities(processingOrders);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    /**
     * 获取所有订单（仅管理员）
     *
     * @return 所有订单列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        List<OrderResponse> orderResponses = OrderResponse.fromEntities(allOrders);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    /**
     * 获取特定日期范围内的订单（仅管理员）
     *
     * @param startDate 开始日期（格式：yyyy-MM-dd）
     * @param endDate   结束日期（格式：yyyy-MM-dd）
     * @return 符合条件的订单列表
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getOrdersByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

        List<Order> orders = orderService.getOrdersBetween(start, end);
        List<OrderResponse> orderResponses = OrderResponse.fromEntities(orders);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }
} 