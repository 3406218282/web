package com.restaurant.orderingsystem.service.impl;

import com.restaurant.orderingsystem.entity.CartItem;
import com.restaurant.orderingsystem.entity.MenuItem;
import com.restaurant.orderingsystem.entity.User;
import com.restaurant.orderingsystem.repository.CartItemRepository;
import com.restaurant.orderingsystem.repository.MenuItemRepository;
import com.restaurant.orderingsystem.repository.UserRepository;
import com.restaurant.orderingsystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * 获取用户的购物车项列表
     *
     * @param user 用户
     * @return 购物车项列表
     */
    @Override
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    /**
     * 根据用户ID获取购物车项列表
     *
     * @param userId 用户ID
     * @return 购物车项列表
     */
    @Override
    public List<CartItem> getCartItemsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        return getCartItems(user);
    }

    /**
     * 添加菜品到购物车
     * 如果菜品已存在于购物车中，则增加数量
     *
     * @param userId     用户ID
     * @param menuItemId 菜品ID
     * @param quantity   数量
     * @return 添加的购物车项
     */
    @Override
    @Transactional
    public CartItem addItemToCart(Long userId, Long menuItemId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("菜品不存在，ID: " + menuItemId));

        // 检查菜品是否可用
        if (menuItem.getAvailability() != MenuItem.Availability.AVAILABLE) {
            throw new RuntimeException("菜品目前不可用: " + menuItem.getName());
        }

        // 检查购物车中是否已存在该菜品
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndMenuItem(user, menuItem);

        if (existingCartItem.isPresent()) {
            // 如果已存在，增加数量
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            // 如果不存在，创建新的购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    /**
     * 更新购物车项数量
     *
     * @param cartItemId 购物车项ID
     * @param quantity   新数量
     * @return 更新后的购物车项
     */
    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在，ID: " + cartItemId));

        if (quantity <= 0) {
            // 如果数量为0或负数，则删除购物车项
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    /**
     * 删除购物车项
     *
     * @param cartItemId 购物车项ID
     */
    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
        cartItemRepository.deleteByUser(user);
    }

    /**
     * 根据ID获取购物车项
     *
     * @param cartItemId 购物车项ID
     * @return 购物车项
     */
    @Override
    public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }
} 