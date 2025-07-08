package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
    private int totalItems;
    
    /**
     * 从购物车项列表创建购物车响应DTO
     * @param cartItems 购物车项列表
     * @return 购物车响应DTO
     */
    public static CartResponse fromCartItems(List<CartItem> cartItems) {
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());
        
        // 计算总金额
        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算总项数
        int totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        
        return new CartResponse(itemResponses, totalAmount, totalItems);
    }
} 