# 餐厅点餐系统后端

## 项目介绍

本项目是一个基于Spring Boot和MySQL开发的餐厅点餐系统后端，提供了完整的餐厅点餐流程所需的API接口，包括用户管理、菜单管理、购物车管理、订单管理等功能。

## 技术选型

- **核心框架**：Spring Boot 2.7.0
- **安全框架**：Spring Security + JWT
- **持久层**：Spring Data JPA + Hibernate
- **数据库**：MySQL 8.0
- **依赖管理**：Maven

## 系统架构

系统采用标准的三层架构设计：

1. **控制层（Controller）**：处理HTTP请求和响应
2. **服务层（Service）**：实现业务逻辑
3. **数据访问层（Repository）**：与数据库交互

## 数据库设计

系统包含以下主要实体及表结构：

### 1. 用户表（users）

| 字段名      | 类型                                   | 说明           |
| ----------- | -------------------------------------- | -------------- |
| id          | INT AUTO_INCREMENT PRIMARY KEY         | 用户ID         |
| username    | VARCHAR(50) NOT NULL UNIQUE            | 用户名         |
| password    | VARCHAR(255) NOT NULL                  | 密码（加密）   |
| role        | ENUM('USER','CHEF','ADMIN') NOT NULL   | 用户角色       |
| full_name   | VARCHAR(100) NOT NULL                  | 真实姓名       |
| email       | VARCHAR(100)                           | 邮箱           |
| phone       | VARCHAR(20)                            | 手机号         |
| status      | ENUM('ACTIVE','INACTIVE','SUSPENDED')  | 用户状态       |
| created_at  | TIMESTAMP                              | 创建时间       |
| updated_at  | TIMESTAMP                              | 更新时间       |

### 2. 菜品分类表（categories）

| 字段名      | 类型                        | 说明         |
| ----------- | --------------------------- | ------------ |
| id          | INT AUTO_INCREMENT PRIMARY KEY | 分类ID     |
| name        | VARCHAR(50) NOT NULL        | 分类名称     |
| description | TEXT                        | 分类描述     |
| created_at  | TIMESTAMP                   | 创建时间     |
| updated_at  | TIMESTAMP                   | 更新时间     |

### 3. 菜单项表（menu_items）

| 字段名      | 类型                                   | 说明           |
| ----------- | -------------------------------------- | -------------- |
| id          | INT AUTO_INCREMENT PRIMARY KEY         | 菜品ID         |
| name        | VARCHAR(100) NOT NULL                  | 菜品名称       |
| description | TEXT                                  | 菜品描述       |
| price       | DECIMAL(10,2) NOT NULL                | 菜品价格       |
| category_id | INT                                   | 分类ID         |
| image_url   | VARCHAR(255)                          | 图片URL        |
| availability| ENUM('AVAILABLE','UNAVAILABLE')        | 是否可用       |
| created_at  | TIMESTAMP                             | 创建时间       |
| updated_at  | TIMESTAMP                             | 更新时间       |

### 4. 购物车表（cart_items）

| 字段名      | 类型                                   | 说明           |
| ----------- | -------------------------------------- | -------------- |
| id          | INT AUTO_INCREMENT PRIMARY KEY         | 购物车项ID     |
| user_id     | INT                                   | 用户ID         |
| menu_item_id| INT                                   | 菜品ID         |
| quantity    | INT NOT NULL DEFAULT 1                 | 菜品数量       |
| created_at  | TIMESTAMP                             | 添加时间       |
| updated_at  | TIMESTAMP                             | 更新时间       |

### 5. 订单表（orders）

| 字段名      | 类型                                   | 说明           |
| ----------- | -------------------------------------- | -------------- |
| id          | INT AUTO_INCREMENT PRIMARY KEY         | 订单ID         |
| user_id     | INT                                   | 用户ID         |
| status      | ENUM('PENDING','PROCESSING','COMPLETED','CANCELLED') NOT NULL | 订单状态 |
| total_price | DECIMAL(10,2) NOT NULL                | 订单总金额     |
| created_at  | TIMESTAMP                             | 创建时间       |
| updated_at  | TIMESTAMP                             | 更新时间       |

### 6. 订单项表（order_items）

| 字段名      | 类型                                   | 说明           |
| ----------- | -------------------------------------- | -------------- |
| id          | INT AUTO_INCREMENT PRIMARY KEY         | 订单项ID       |
| order_id    | INT                                   | 订单ID         |
| menu_item_id| INT                                   | 菜品ID         |
| quantity    | INT NOT NULL                          | 菜品数量       |
| price       | DECIMAL(10,2) NOT NULL                | 下单时菜品价格 |
| created_at  | TIMESTAMP                             | 添加时间       |

> 注：所有外键均有相应的外键约束，保证数据一致性。

## 主要功能

### 1. 用户认证与授权

- 用户登录：`POST /api/auth/login`
- 用户登出：`POST /api/auth/logout`

### 2. 菜单管理

- 获取所有菜品：`GET /api/menu`
- 获取菜品详情：`GET /api/menu/{id}`
- 按分类获取菜品：`GET /api/menu/category/{categoryId}`
- 搜索菜品：`GET /api/menu/search?name={name}`
- 添加菜品（管理员）：`POST /api/menu`
- 更新菜品（管理员）：`PUT /api/menu/{id}`
- 删除菜品（管理员）：`DELETE /api/menu/{id}`

### 3. 分类管理

- 获取所有分类：`GET /api/categories`
- 获取分类详情：`GET /api/categories/{id}`
- 添加分类（管理员）：`POST /api/categories`
- 更新分类（管理员）：`PUT /api/categories/{id}`
- 删除分类（管理员）：`DELETE /api/categories/{id}`

### 4. 购物车管理

- 获取购物车：`GET /api/cart`
- 添加菜品到购物车：`POST /api/cart`
- 更新购物车项数量：`PUT /api/cart/{cartItemId}?quantity={quantity}`
- 删除购物车项：`DELETE /api/cart/{cartItemId}`
- 清空购物车：`DELETE /api/cart`

### 5. 订单管理

- 提交订单：`POST /api/orders`
- 获取用户订单：`GET /api/orders/my-orders`
- 获取订单详情：`GET /api/orders/{orderId}`
- 取消订单：`PUT /api/orders/{orderId}/cancel`
- 更新订单状态（厨师/管理员）：`PUT /api/orders/{orderId}/status?status={status}`
- 获取待处理订单（厨师/管理员）：`GET /api/orders/pending`
- 获取处理中订单（厨师/管理员）：`GET /api/orders/processing`
- 获取所有订单（管理员）：`GET /api/orders`
- 按日期范围获取订单（管理员）：`GET /api/orders/date-range?startDate={startDate}&endDate={endDate}`

## 用户角色与权限

系统定义了三种用户角色：

1. **普通用户（USER）**：可以浏览菜单、管理购物车、提交订单、查看自己的订单
2. **厨师（CHEF）**：可以查看和处理订单
3. **管理员（ADMIN）**：拥有所有权限，可以管理菜单、分类、用户、查看所有订单等

## 使用方法

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 配置说明

在`application.properties`文件中配置数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
```

### 运行项目

1. 克隆项目到本地
2. 配置数据库连接信息
3. 使用Maven构建项目：`mvn clean package`
4. 运行生成的JAR文件：`java -jar target/ordering-system-0.0.1-SNAPSHOT.jar`

### 初始用户

系统在首次启动时会自动创建以下用户：

- **管理员**：用户名 `admin`，密码 `admin123`
- **厨师**：用户名 `chef`，密码 `chef123`
- **普通用户**：用户名 `user`，密码 `user123`

## API认证

除了登录接口和部分公开菜单接口外，其他API都需要在请求头中添加JWT令牌进行认证：

```
Authorization: Bearer 你的JWT令牌
```

## 项目结构

```
src/main/java/com/restaurant/orderingsystem/
├── config/             # 配置类
├── controller/         # 控制器
├── dto/                # 数据传输对象
├── entity/             # 实体类
├── exception/          # 异常处理
├── repository/         # 数据访问层
├── security/           # 安全相关
├── service/            # 服务层
│   └── impl/           # 服务实现
└── util/               # 工具类
```