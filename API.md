# 餐厅点餐系统 API 文档 

## 基础URL (Base URL)
`http://localhost:8080/api`

## 认证 (Authentication)
除 `/auth/login` 外所有接口都需要JWT认证。在 `Authorization` 头中包含 `Bearer {token}`。

### 登录 (Login)

**POST** `/auth/login`
Request:

```json
{
  "username": "string",
  "password": "string"
}
```
Response:
```json
{
  "token": "string",
  "expiresIn": 3600
}
```

## 分类 (Categories)
### 获取所有分类 (Get All Categories)
**GET** `/categories`
Response:
```json
[
  {
    "id": 1,
    "name": "Appetizers"
  }
]
```

### 创建分类 (管理员) (Create Category - Admin)
**POST** `/categories`
Request:
```json
{
  "name": "string"
}
```

## 菜单项 (Menu Items)
### 按分类获取菜单项 (Get Menu Items by Category)
**GET** `/menu-items?categoryId={id}`
Response:

```json
[
  {
    "id": 1,
    "name": "Spring Rolls",
    "description": "Crispy vegetable spring rolls",
    "price": 5.99,
    "categoryId": 1
  }
]
```

## 购物车 (Cart)
### 添加到购物车 (Add to Cart)

**POST** `/cart/items`
Request:

```json
{
  "menuItemId": 1,
  "quantity": 2
}
```

### 获取购物车 (Get Cart)
**GET** `/cart`
Response:
```json
{
  "items": [
    {
      "id": 1,
      "menuItemId": 1,
      "name": "Spring Rolls",
      "quantity": 2,
      "price": 5.99
    }
  ],
  "total": 11.98
}
```

## 订单 (Orders)
### 下单 (Place Order)
**POST** `/orders`
Response:
```json
{
  "id": 1,
  "status": "PENDING",
  "items": [
    {
      "menuItemId": 1,
      "name": "Spring Rolls",
      "quantity": 2,
      "price": 5.99
    }
  ],
  "total": 11.98,
  "createdAt": "2025-07-08T21:15:00"
}
```

## 错误响应 (Error Responses)
```json
{
  "status": 401,
  "message": "Unauthorized"
}
```
```json
{
  "status": 404,
  "message": "Menu item not found"
}
