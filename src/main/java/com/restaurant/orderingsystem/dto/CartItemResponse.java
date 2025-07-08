package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.CartItem;

import java.math.BigDecimal;

/**
 * 购物车项响应DTO
 */
public class CartItemResponse {
    
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    public CartItemResponse() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    
    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
    
    public String getMenuItemImageUrl() { return menuItemImageUrl; }
    public void setMenuItemImageUrl(String menuItemImageUrl) { this.menuItemImageUrl = menuItemImageUrl; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
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