package com.restaurant.orderingsystem.service;

import com.restaurant.orderingsystem.entity.CartItem;
import com.restaurant.orderingsystem.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 购物车服务接口
 * 提供购物车管理功能
 */
public interface CartService {
    
    /**
     * 获取用户的购物车项列表
     * @param user 用户
     * @return 购物车项列表
     */
    List<CartItem> getCartItems(User user);
    
    /**
     * 根据用户ID获取购物车项列表
     * @param userId 用户ID
     * @return 购物车项列表
     */
    List<CartItem> getCartItemsByUserId(Long userId);
    
    /**
     * 添加菜品到购物车
     * @param userId 用户ID
     * @param menuItemId 菜品ID
     * @param quantity 数量
     * @return 添加的购物车项
     */
    CartItem addItemToCart(Long userId, Long menuItemId, Integer quantity);
    
    /**
     * 更新购物车项数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     * @return 更新后的购物车项
     */
    CartItem updateCartItemQuantity(Long cartItemId, Integer quantity);
    
    /**
     * 删除购物车项
     * @param cartItemId 购物车项ID
     */
    void removeCartItem(Long cartItemId);
    
    /**
     * 清空用户购物车
     * @param userId 用户ID
     */
    void clearCart(Long userId);
    
    /**
     * 根据ID获取购物车项
     * @param cartItemId 购物车项ID
     * @return 购物车项
     */
    Optional<CartItem> getCartItemById(Long cartItemId);
} 