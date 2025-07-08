package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 购物车项响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    /**
     * 从购物车项实体创建响应DTO
     * @param cartItem 购物车项实体
     * @return 购物车项响应DTO
     */
    public static CartItemResponse fromEntity(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setMenuItemId(cartItem.getMenuItem().getId());
        response.setMenuItemName(cartItem.getMenuItem().getName());
        response.setMenuItemImageUrl(cartItem.getMenuItem().getImageUrl());
        response.setPrice(cartItem.getMenuItem().getPrice());
        response.setQuantity(cartItem.getQuantity());
        
        // 计算总价
        BigDecimal totalPrice = cartItem.getMenuItem().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        response.setTotalPrice(totalPrice);
        
        return response;
    }
} 