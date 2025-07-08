package com.restaurant.orderingsystem.controller;

import com.restaurant.orderingsystem.dto.AddToCartRequest;
import com.restaurant.orderingsystem.dto.CartResponse;
import com.restaurant.orderingsystem.entity.CartItem;
import com.restaurant.orderingsystem.security.UserDetailsImpl;
import com.restaurant.orderingsystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 购物车控制器
 * 提供购物车管理的REST API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取当前用户的购物车
     *
     * @param userDetails 当前登录用户信息
     * @return 购物车响应
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userDetails.getId());
        CartResponse cartResponse = CartResponse.fromCartItems(cartItems);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    /**
     * 添加菜品到购物车
     *
     * @param userDetails 当前登录用户信息
     * @param request     添加到购物车请求
     * @return 更新后的购物车响应
     */
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddToCartRequest request) {
        cartService.addItemToCart(userDetails.getId(), request.getMenuItemId(), request.getQuantity());
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userDetails.getId());
        CartResponse cartResponse = CartResponse.fromCartItems(cartItems);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    /**
     * 更新购物车项数量
     *
     * @param userDetails 当前登录用户信息
     * @param cartItemId  购物车项ID
     * @param quantity    新数量
     * @return 更新后的购物车响应
     */
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("cartItemId") Long cartItemId,
            @RequestParam("quantity") Integer quantity) {
        cartService.updateCartItemQuantity(cartItemId, quantity);
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userDetails.getId());
        CartResponse cartResponse = CartResponse.fromCartItems(cartItems);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    /**
     * 删除购物车项
     *
     * @param userDetails 当前登录用户信息
     * @param cartItemId  购物车项ID
     * @return 更新后的购物车响应
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("cartItemId") Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userDetails.getId());
        CartResponse cartResponse = CartResponse.fromCartItems(cartItems);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    /**
     * 清空购物车
     *
     * @param userDetails 当前登录用户信息
     * @return 成功消息
     */
    @DeleteMapping
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.clearCart(userDetails.getId());
        return new ResponseEntity<>("购物车已清空", HttpStatus.OK);
    }
} 