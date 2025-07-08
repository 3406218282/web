package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private Long userId;
    private String userName;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 从订单实体创建响应DTO
     * @param order 订单实体
     * @return 订单响应DTO
     */
    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getUsername());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        
        // 设置订单项
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());
        response.setItems(itemResponses);
        
        return response;
    }
    
    /**
     * 从订单列表创建响应DTO列表
     * @param orders 订单列表
     * @return 订单响应DTO列表
     */
    public static List<OrderResponse> fromEntities(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
} 