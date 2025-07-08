package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单项响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    /**
     * 从订单项实体创建响应DTO
     * @param orderItem 订单项实体
     * @return 订单项响应DTO
     */
    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setMenuItemId(orderItem.getMenuItem().getId());
        response.setMenuItemName(orderItem.getMenuItem().getName());
        response.setMenuItemImageUrl(orderItem.getMenuItem().getImageUrl());
        response.setPrice(orderItem.getPrice());
        response.setQuantity(orderItem.getQuantity());
        
        // 计算总价
        BigDecimal totalPrice = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        response.setTotalPrice(totalPrice);
        
        return response;
    }
} 