### 需求分析

#### **1**1. 系统角色及权限

#### **1.1 前台点餐用户**

- **权限描述**：
    - **浏览菜单**：可以查看所有可用的菜品和菜品分类。每个菜品展示的内容包括：菜品名称、价格、图片、描述、可选规格等。
    - **购物车管理**：可以将菜品加入购物车、删除购物车中的菜品、修改菜品数量。
    - **订单提交**：从购物车中提交订单，订单包含所选菜品、数量和总金额等信息。
    - **查看订单状态**：可以查看自己历史订单的状态，状态包括：已提交、待处理、已完成、已取消等。

#### **1.2 后厨用户**

- **权限描述**：
    - **查看待处理订单**：可以查看所有订单列表，并筛选出未处理的订单。订单信息包括：订单编号、顾客信息、菜品详情、下单时间等。
    - **更新订单状态**：根据订单的制作进度，后厨人员可以更新订单的状态，如：
        - **已开始制作**：表示后厨已开始制作该订单中的菜品。
        - **制作中**：订单正在处理中。
        - **已完成**：所有菜品已制作完成，可以交给配送或顾客取餐。

#### **1.3 管理员**

- **权限描述**：
    - **菜单管理**：管理员可以对系统中的菜单进行增、删、改、查操作。例如：
        - **添加菜品**：包括菜品名称、分类、价格、描述等。
        - **删除菜品**：删除指定的菜品。
        - **编辑菜品信息**：修改菜品的价格、描述等。
        - **菜品分类管理**：管理员可以新增、修改或删除菜品分类。
    - **查看所有订单**：管理员可以查看系统中所有订单的状态，包括前台用户和后厨操作的订单状态。
    - **统计分析**：管理员可以查看订单统计数据（如：总收入、菜品销售量、订单完成率等），并根据数据做出管理决策。
    - **用户管理**：管理员可以查看所有系统用户（包括前台用户、后厨用户和管理员）的信息，并进行管理（如：禁用用户、重置密码等）。
    - **系统设置管理**：管理员可以设置菜品的价格区间、优惠设置、系统配置等。

------

### **2. 登录功能**

- **用户登录**：
    - 用户通过提供 **用户名** 和 **密码** 进行身份验证。
    - 登录后，系统根据用户的角色（前台用户、后厨用户、管理员）展示不同的功能界面。
    - 登录过程包括：
        - 输入用户名和密码。
        - 后端验证用户名和密码是否正确。
        - 如果用户名和密码正确，则生成一个 **JWT token**，用于后续请求的身份验证。
        - 登录成功后，将用户的角色信息存储在会话或 Token 中。
- **登出功能**：
    - 用户可以主动登出系统，登出时清除会话信息。
    - 登出后，JWT token 失效，前端无法再进行权限验证通过的操作。
    - 可以删除前端存储的 token 信息，或使其失效。

------

### **3. 系统功能模块**

#### **3.1 用户管理（登录与登出）**

- **登录接口**：
    - **请求**：
        - `POST /api/login`
        - 请求体：包含 `username` 和 `password` 字段。
    - **返回**：
        - 如果身份验证通过，返回 **JWT token** 和 **用户角色信息**。
        - 如果验证失败，返回错误信息（如：用户名或密码错误）。
- **登出接口**：
    - **请求**：
        - `POST /api/logout`
    - **返回**：
        - 清除会话或 Token，登出成功返回状态码 `200 OK`。

------

#### **3.2 菜单管理**

##### **管理员功能**：

- **菜单增删改查**：
    - `GET /api/menu`: 获取所有菜品。
    - `POST /api/menu`: 添加新菜品（需要提供菜品名称、分类、价格、描述等）。
    - `PUT /api/menu/{id}`: 修改菜品信息（可以修改价格、描述等）。
    - `DELETE /api/menu/{id}`: 删除指定的菜品。
- **菜品分类管理**：
    - `GET /api/categories`: 获取所有菜品分类。
    - `POST /api/categories`: 添加新的菜品分类。
    - `PUT /api/categories/{id}`: 修改菜品分类信息。
    - `DELETE /api/categories/{id}`: 删除指定的菜品分类。

##### **前台用户功能**：

- **查看菜单**：
    - `GET /api/menu`: 获取所有可用的菜品列表。
    - `GET /api/menu/{id}`: 获取指定菜品的详细信息。
- **浏览菜品分类**：
    - `GET /api/categories`: 获取所有菜品分类。

------

#### **3.3 购物车与订单管理**

##### **前台用户功能**：

- **购物车管理**：
    - `POST /api/cart`: 向购物车添加菜品（需要提供菜品 ID 和数量）。
    - `DELETE /api/cart/{id}`: 从购物车中删除指定菜品。
    - `GET /api/cart`: 获取当前用户的购物车内容。
- **提交订单**：
    - `POST /api/order`: 提交订单，包含购物车中所有菜品的详细信息、数量和总价等。
    - **返回**：订单创建成功，返回订单编号和当前订单状态（如：已提交）。
- **查看订单状态**：
    - `GET /api/orders`: 获取当前用户所有历史订单的状态，状态包括：已提交、待处理、已完成等。
    - `GET /api/orders/{id}`: 获取指定订单的详细信息和状态。

##### **后厨功能**：

- **查看待处理订单**：
    - `GET /api/orders/pending`: 获取所有待处理的订单。
    - 订单信息包括：订单编号、顾客信息、菜品详情、状态等。
- **更新订单状态**：
    - `PUT /api/orders/{id}/status`: 更新订单的处理状态，如：
        - `已开始制作`
        - `制作中`
        - `已完成`

##### **管理员功能**：

- **查看所有订单**：
    - `GET /api/orders`: 获取所有订单的列表，包括前台和后厨的订单状态。
- **订单统计与分析**：
    - `GET /api/orders/stats`: 获取订单统计信息，如：总订单数、总收入、菜品销售量等。

------

#### **3.4 后厨与管理员管理订单**

##### **后厨功能**：

- **查看待处理订单**：
    - `GET /api/orders/pending`: 查看所有待处理的订单。
- **更新订单状态**：
    - `PUT /api/orders/{id}/status`: 根据订单进度更新状态。

##### **管理员功能**：

- **查看所有订单**：
    - `GET /api/orders`: 查看所有订单，包括前台和后厨的订单状态。
- **统计分析**：
    - `GET /api/orders/stats`: 统计订单信息，如销售量、收入、订单状态等。
- **用户管理**：
    - `GET /api/users`: 获取所有用户的信息。
    - `POST /api/users`: 添加新用户（包括前台、后厨和管理员角色）。
    - `PUT /api/users/{id}`: 修改用户信息（如角色、密码等）。
    - `DELETE /api/users/{id}`: 删除用户。

### **数据库设计**

### **1. 用户表 (`users`)**

用户表用于存储系统中所有用户的信息，包括前台用户、后厨用户和管理员。

```
sqlCopyEditCREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 用户ID，自增
    username VARCHAR(50) NOT NULL UNIQUE,            -- 用户名，唯一
    password VARCHAR(255) NOT NULL,                  -- 密码，存储加密后的密码
    role ENUM('user', 'chef', 'admin') NOT NULL,     -- 用户角色，值为 'user'、'chef'、'admin' 中之一
    full_name VARCHAR(100) NOT NULL,                 -- 用户全名
    email VARCHAR(100),                              -- 用户电子邮件（可选）
    phone VARCHAR(20),                               -- 用户手机号（可选）
    status ENUM('active', 'inactive', 'suspended') DEFAULT 'active', -- 用户状态（激活、非激活、禁用）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);
```

#### 字段解释：

- `id`：每个用户的唯一标识符。
- `username`：用户登录名，必须唯一。
- `password`：用户的密码，通常存储加密后的密码（如使用 bcrypt）。
- `role`：用户角色，标识用户是前台用户、后厨人员还是管理员。
- `full_name`：用户的真实姓名。
- `email` 和 `phone`：可选字段，用于记录用户的联系方式。
- `status`：用户状态，表示当前用户是否启用、禁用或被暂停。
- `created_at` 和 `updated_at`：记录用户账户的创建时间和最后更新时间。

------

### **2. 菜品表 (`menu_items`)**

菜品表用于存储菜单中的菜品信息。

```
sqlCopyEditCREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 菜品ID
    name VARCHAR(100) NOT NULL,                      -- 菜品名称
    description TEXT,                                -- 菜品描述
    price DECIMAL(10, 2) NOT NULL,                   -- 菜品价格
    category_id INT,                                 -- 菜品分类ID，关联 `categories` 表
    image_url VARCHAR(255),                          -- 菜品图片URL
    availability ENUM('available', 'unavailable') DEFAULT 'available', -- 菜品是否可用
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    FOREIGN KEY (category_id) REFERENCES categories(id) -- 外键约束，关联分类表
);
```

#### 字段解释：

- `id`：菜品的唯一标识符。
- `name`：菜品名称。
- `description`：菜品的详细描述。
- `price`：菜品的价格，保留两位小数。
- `category_id`：菜品所属分类的ID，关联到 `categories` 表中的分类。
- `image_url`：菜品的图片URL，便于展示。
- `availability`：表示菜品是否可以购买，`available` 表示可用，`unavailable` 表示不可用。
- `created_at` 和 `updated_at`：记录菜品的创建时间和最后更新时间。

------

### **3. 菜品分类表 (`categories`)**

菜品分类表用于存储菜品的分类信息。

```
sqlCopyEditCREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 分类ID
    name VARCHAR(50) NOT NULL,                       -- 分类名称
    description TEXT,                                -- 分类描述
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
);
```

#### 字段解释：

- `id`：菜品分类的唯一标识符。
- `name`：分类名称，如“主食”、“饮品”、“小吃”等。
- `description`：分类的描述信息，用于详细说明此分类包含的菜品类型。
- `created_at` 和 `updated_at`：记录分类的创建时间和最后更新时间。

------

### **4. 购物车表 (`cart_items`)**

购物车表用于存储用户添加到购物车中的菜品信息。

```
sqlCopyEditCREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 购物车项ID
    user_id INT,                                     -- 用户ID，关联 `users` 表
    menu_item_id INT,                                -- 菜品ID，关联 `menu_items` 表
    quantity INT NOT NULL DEFAULT 1,                  -- 菜品数量，默认为1
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 添加时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
    FOREIGN KEY (user_id) REFERENCES users(id),      -- 外键约束，关联用户表
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) -- 外键约束，关联菜品表
);
```

#### 字段解释：

- `id`：购物车项的唯一标识符。
- `user_id`：用户ID，标识购物车属于哪个用户。
- `menu_item_id`：菜品ID，表示购物车中添加的具体菜品。
- `quantity`：菜品的数量，默认为1。
- `created_at` 和 `updated_at`：记录每次菜品加入购物车的时间。

------

### **5. 订单表 (`orders`)**

订单表用于存储用户提交的订单信息。

```
sqlCopyEditCREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 订单ID
    user_id INT,                                     -- 用户ID，关联 `users` 表
    status ENUM('pending', 'processing', 'completed', 'cancelled') NOT NULL,  -- 订单状态
    total_price DECIMAL(10, 2) NOT NULL,             -- 订单总金额
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 订单创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 订单更新时间
    FOREIGN KEY (user_id) REFERENCES users(id)      -- 外键约束，关联用户表
);
```

#### 字段解释：

- `id`：订单的唯一标识符。
- `user_id`：用户ID，标识此订单属于哪个用户。
- `status`：订单状态，`pending` 为待处理，`processing` 为处理中，`completed` 为已完成，`cancelled` 为已取消。
- `total_price`：订单的总金额。
- `created_at` 和 `updated_at`：记录订单的创建时间和最后更新时间。

------

### **6. 订单详情表 (`order_items`)**

订单详情表用于存储订单中的具体菜品信息。

```
sqlCopyEditCREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 订单项ID
    order_id INT,                                    -- 订单ID，关联 `orders` 表
    menu_item_id INT,                                -- 菜品ID，关联 `menu_items` 表
    quantity INT NOT NULL,                           -- 菜品数量
    price DECIMAL(10, 2) NOT NULL,                   -- 菜品价格（此价格为下单时菜品的价格，防止价格变动影响订单）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 订单项添加时间
    FOREIGN KEY (order_id) REFERENCES orders(id),    -- 外键约束，关联订单表
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) -- 外键约束，关联菜品表
);
```

#### 字段解释：

- `id`：订单项的唯一标识符。
- `order_id`：订单ID，标识此订单项属于哪个订单。
- `menu_item_id`：菜品ID，标识订单中包含的菜品。
- `quantity`：此菜品的数量。
- `price`：此菜品在订单中的价格（以防菜品价格发生变化，订单历史数据不受影响）。
- `created_at`：记录每个订单项的添加时间。

------

### **7. 支付记录表 (`payments`)**

支付记录表用于存储用户的支付信息。

```
sqlCopyEditCREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- 支付记录ID
    order_id INT,                                    -- 订单ID，关联 `orders` 表
    payment_method ENUM('credit_card', 'paypal', 'cash', 'other') NOT NULL, -- 支付方式
    amount DECIMAL(10, 2) NOT NULL,                   -- 支付金额
    payment_status ENUM('pending', 'paid', 'failed') NOT NULL, -- 支付状态
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 支付时间
    FOREIGN KEY (order_id) REFERENCES orders(id)     -- 外键约束，关联订单表
);
```

#### 字段解释：

- `id`：支付记录的唯一标识符。
- `order_id`：订单ID，表示此支付记录关联哪个订单。
- `payment_method`：支付方式，记录支付的方式，如信用卡、PayPal、现金等。
- `amount`：支付金额，通常与订单总金额一致。
- `payment_status`：支付状态，`pending` 表示支付待处理，`paid` 表示支付完成，`failed` 表示支付失败。
- `payment_date`：支付时间，记录支付发生的时间。

------

### **后端功能设计**

#### **1. 用户管理 API**

- **登录接口**：`POST /api/login`
    - 根据用户名和密码验证登录信息，返回 JWT（JSON Web Token）或 session token。
    - 根据角色（前台、后厨、管理员），返回相应的权限和用户信息。
- **登出接口**：`POST /api/logout`
    - 使用户登出，清除会话或 token。

```
javaCopyEdit@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            String token = JwtUtils.generateToken(user);  // 使用 JWT 生成 token
            return ResponseEntity.ok(new LoginResponse(token, user.getRole()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> logout() {
        // 退出操作：清除 token 或会话
        return ResponseEntity.ok("Logout successful");
    }
}
```

#### **2. 菜单管理 API（仅管理员可用）**

- **添加菜品**：`POST /api/menu`
- **修改菜品**：`PUT /api/menu/{id}`
- **删除菜品**：`DELETE /api/menu/{id}`
- **获取所有菜品**：`GET /api/menu`
- **获取菜品分类**：`GET /api/menu/categories`

```
javaCopyEdit@Controller
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String addMenuItem(@RequestBody MenuItem menuItem) {
        menuService.addMenuItem(menuItem);
        return "Menu item added successfully";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateMenuItem(@PathVariable("id") long id, @RequestBody MenuItem menuItem) {
        menuService.updateMenuItem(id, menuItem);
        return "Menu item updated successfully";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMenuItem(@PathVariable("id") long id) {
        menuService.deleteMenuItem(id);
        return "Menu item deleted successfully";
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public List<Category> getAllCategories() {
        return menuService.getAllCategories();
    }
}
```

#### **3. 购物车管理 API**

- **添加菜品到购物车**：`POST /api/cart`
- **查看购物车内容**：`GET /api/cart`
- **修改购物车中的菜品数量**：`PUT /api/cart`
- **删除购物车中的菜品**：`DELETE /api/cart`

#### **4. 订单管理 API**

- **提交订单**：`POST /api/orders`
- **查看订单详情**：`GET /api/orders/{orderId}`
- **查看所有订单**（管理员）：`GET /api/orders`
- **查看待处理订单**（后厨）：`GET /api/orders/pending`

#### **5. 后厨管理 API**

- **更新订单状态**（后厨）：`PUT /api/orders/{orderId}/status`

```
javaCopyEdit@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 提交订单
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String submitOrder(@RequestBody Order order) {
        orderService.submitOrder(order);
        return "Order submitted successfully";
    }

    // 获取订单详情
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Order getOrderDetails(@PathVariable("orderId") long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    // 查看待处理订单（后厨）
    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    // 更新订单状态（后厨）
    @RequestMapping(value = "/{orderId}/status", method = RequestMethod.PUT)
    @ResponseBody
    public String updateOrderStatus(@PathVariable("orderId") long orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "Order status updated";
    }
}
```