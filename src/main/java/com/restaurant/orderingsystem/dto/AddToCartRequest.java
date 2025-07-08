package com.restaurant.orderingsystem.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加到购物车的请求DTO
 */
@Data
public class AddToCartRequest {
    
    @NotNull(message = "菜品ID不能为空")
    private Long menuItemId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
} 