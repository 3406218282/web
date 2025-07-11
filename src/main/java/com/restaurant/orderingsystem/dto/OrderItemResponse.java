package com.restaurant.orderingsystem.dto;

import com.restaurant.orderingsystem.entity.OrderItem;

import java.math.BigDecimal;

/**
 * 订单项响应DTO
 */
public class OrderItemResponse {
    
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    public OrderItemResponse() {}
    
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