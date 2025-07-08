package com.restaurant.orderingsystem.config;

import com.restaurant.orderingsystem.entity.Category;
import com.restaurant.orderingsystem.entity.MenuItem;
import com.restaurant.orderingsystem.entity.User;
import com.restaurant.orderingsystem.repository.CategoryRepository;
import com.restaurant.orderingsystem.repository.MenuItemRepository;
import com.restaurant.orderingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if we need to initialize the database
        if (userRepository.count() == 0) {
            initializeUsers();
        }

        if (categoryRepository.count() == 0) {
            initializeCategories();
        }
    }

    private void initializeUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        admin.setFullName("System Administrator");
        admin.setEmail("admin@restaurant.com");
        admin.setStatus(User.UserStatus.ACTIVE);

        // Create chef user
        User chef = new User();
        chef.setUsername("chef");
        chef.setPassword(passwordEncoder.encode("chef123"));
        chef.setRole(User.Role.CHEF);
        chef.setFullName("Head Chef");
        chef.setEmail("chef@restaurant.com");
        chef.setStatus(User.UserStatus.ACTIVE);

        // Create regular user
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole(User.Role.USER);
        user.setFullName("Regular User");
        user.setEmail("user@restaurant.com");
        user.setStatus(User.UserStatus.ACTIVE);

        // Save users
        userRepository.saveAll(Arrays.asList(admin, chef, user));
        System.out.println("Users initialized successfully");
    }

    private void initializeCategories() {
        // Create categories
        Category mainCourse = new Category();
        mainCourse.setName("Main Course");
        mainCourse.setDescription("Main dishes that form the primary part of a meal");
        categoryRepository.save(mainCourse);

        Category appetizer = new Category();
        appetizer.setName("Appetizer");
        appetizer.setDescription("Small dishes served before the main course");
        categoryRepository.save(appetizer);

        Category dessert = new Category();
        dessert.setName("Dessert");
        dessert.setDescription("Sweet dishes typically served at the end of a meal");
        categoryRepository.save(dessert);

        Category beverage = new Category();
        beverage.setName("Beverage");
        beverage.setDescription("Drinks to accompany your meal");
        categoryRepository.save(beverage);

        // Create menu items
        createMenuItem("Grilled Chicken", "Tender grilled chicken with herbs and spices", new BigDecimal("15.99"), mainCourse);
        createMenuItem("Beef Steak", "Juicy beef steak cooked to perfection", new BigDecimal("24.99"), mainCourse);
        createMenuItem("Vegetable Pasta", "Pasta with fresh seasonal vegetables", new BigDecimal("12.99"), mainCourse);

        createMenuItem("Garlic Bread", "Toasted bread with garlic butter", new BigDecimal("4.99"), appetizer);
        createMenuItem("Chicken Wings", "Spicy chicken wings with dipping sauce", new BigDecimal("9.99"), appetizer);
        createMenuItem("Salad", "Fresh garden salad with vinaigrette", new BigDecimal("6.99"), appetizer);

        createMenuItem("Chocolate Cake", "Rich chocolate cake with icing", new BigDecimal("7.99"), dessert);
        createMenuItem("Ice Cream", "Vanilla ice cream with toppings", new BigDecimal("5.99"), dessert);
        createMenuItem("Fruit Tart", "Sweet tart with fresh seasonal fruits", new BigDecimal("8.99"), dessert);

        createMenuItem("Soda", "Refreshing carbonated beverage", new BigDecimal("2.99"), beverage);
        createMenuItem("Coffee", "Freshly brewed coffee", new BigDecimal("3.99"), beverage);
        createMenuItem("Tea", "Hot tea with optional milk and sugar", new BigDecimal("2.99"), beverage);

        System.out.println("Categories and menu items initialized successfully");
    }

    private void createMenuItem(String name, String description, BigDecimal price, Category category) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setCategory(category);
        menuItem.setAvailability(MenuItem.Availability.AVAILABLE);
        menuItemRepository.save(menuItem);
    }
} 