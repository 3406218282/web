package com.restaurant.orderingsystem.controller;

import com.restaurant.orderingsystem.entity.MenuItem;
import com.restaurant.orderingsystem.service.CategoryService;
import com.restaurant.orderingsystem.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单项控制器
 * 提供菜单项相关的REST API接口
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有菜单项
     *
     * @return 菜单项列表
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    /**
     * 根据ID获取菜单项
     *
     * @param id 菜单项ID
     * @return 菜单项对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable("id") Long id) {
        return menuItemService.getMenuItemById(id)
                .map(menuItem -> new ResponseEntity<>(menuItem, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据分类ID获取菜单项
     *
     * @param categoryId 分类ID
     * @return 菜单项列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByCategoryId(categoryId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    /**
     * 根据名称搜索菜单项
     *
     * @param name 菜品名称
     * @return 菜单项列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItems(@RequestParam("name") String name) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByName(name);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    /**
     * 创建菜单项（仅管理员）
     *
     * @param menuItem 菜单项对象
     * @return 创建的菜单项
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        MenuItem savedMenuItem = menuItemService.createMenuItem(menuItem);
        return new ResponseEntity<>(savedMenuItem, HttpStatus.CREATED);
    }

    /**
     * 更新菜单项（仅管理员）
     *
     * @param id 菜单项ID
     * @param menuItem 更新的菜单项详情
     * @return 更新后的菜单项
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable("id") Long id, @Valid @RequestBody MenuItem menuItem) {
        if (!menuItemService.getMenuItemById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, menuItem);
        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
    }

    /**
     * 更新菜单项可用性（仅管理员）
     *
     * @param id 菜单项ID
     * @param availability 可用性状态
     * @return 更新后的菜单项
     */
    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItem> updateMenuItemAvailability(
            @PathVariable("id") Long id,
            @RequestParam("availability") MenuItem.Availability availability) {
        if (!menuItemService.getMenuItemById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuItem updatedMenuItem = menuItemService.updateMenuItemAvailability(id, availability);
        return new ResponseEntity<>(updatedMenuItem, HttpStatus.OK);
    }

    /**
     * 删除菜单项（仅管理员）
     *
     * @param id 菜单项ID
     * @return 删除状态
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteMenuItem(@PathVariable("id") Long id) {
        if (!menuItemService.getMenuItemById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuItemService.deleteMenuItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 