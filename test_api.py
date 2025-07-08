import requests
import json

BASE_URL = 'http://localhost:8080'
USERS = [
    {'username': 'admin', 'password': '123456'},
    {'username': 'chef', 'password': '123456'},
    {'username': 'user', 'password': '123456'},
]

# 所有接口定义（路径、方法、参数示例）
ENDPOINTS = [
    # Auth
    {'name': '登录', 'method': 'POST', 'url': '/api/auth/login', 'json': {'username': None, 'password': None}, 'auth': False},
    {'name': '登出', 'method': 'POST', 'url': '/api/auth/logout', 'auth': True},
    # Category
    {'name': '获取所有分类', 'method': 'GET', 'url': '/api/categories', 'auth': True},
    {'name': '获取分类详情', 'method': 'GET', 'url': '/api/categories/1', 'auth': True},
    {'name': '创建分类', 'method': 'POST', 'url': '/api/categories', 'json': {'name': '测试分类'}, 'auth': True},
    {'name': '更新分类', 'method': 'PUT', 'url': '/api/categories/1', 'json': {'name': '更新分类'}, 'auth': True},
    {'name': '删除分类', 'method': 'DELETE', 'url': '/api/categories/1', 'auth': True},
    # MenuItem
    {'name': '获取所有菜单项', 'method': 'GET', 'url': '/api/menu', 'auth': True},
    {'name': '获取菜单项详情', 'method': 'GET', 'url': '/api/menu/1', 'auth': True},
    {'name': '按分类获取菜单项', 'method': 'GET', 'url': '/api/menu/category/1', 'auth': True},
    {'name': '搜索菜单项', 'method': 'GET', 'url': '/api/menu/search?keyword=test', 'auth': True},
    {'name': '创建菜单项', 'method': 'POST', 'url': '/api/menu', 'json': {'name': '测试菜品', 'price': 10.0, 'categoryId': 1, 'description': 'desc', 'available': True}, 'auth': True},
    {'name': '更新菜单项', 'method': 'PUT', 'url': '/api/menu/1', 'json': {'name': '更新菜品', 'price': 12.0, 'categoryId': 1, 'description': 'desc2', 'available': True}, 'auth': True},
    {'name': '设置菜单项可用性', 'method': 'PUT', 'url': '/api/menu/1/availability', 'json': {'available': False}, 'auth': True},
    {'name': '删除菜单项', 'method': 'DELETE', 'url': '/api/menu/1', 'auth': True},
    # Cart
    {'name': '获取购物车', 'method': 'GET', 'url': '/api/cart', 'auth': True},
    {'name': '添加到购物车', 'method': 'POST', 'url': '/api/cart', 'json': {'menuItemId': 1, 'quantity': 1}, 'auth': True},
    {'name': '更新购物车项', 'method': 'PUT', 'url': '/api/cart/1', 'json': {'quantity': 2}, 'auth': True},
    {'name': '删除购物车项', 'method': 'DELETE', 'url': '/api/cart/1', 'auth': True},
    {'name': '清空购物车', 'method': 'DELETE', 'url': '/api/cart', 'auth': True},
    # Order
    {'name': '创建订单', 'method': 'POST', 'url': '/api/orders', 'json': {'address': '测试地址', 'paymentMethod': 'CASH'}, 'auth': True},
    {'name': '获取我的订单', 'method': 'GET', 'url': '/api/orders/my-orders', 'auth': True},
    {'name': '获取订单详情', 'method': 'GET', 'url': '/api/orders/1', 'auth': True},
    {'name': '取消订单', 'method': 'PUT', 'url': '/api/orders/1/cancel', 'auth': True},
    {'name': '更新订单状态', 'method': 'PUT', 'url': '/api/orders/1/status', 'json': {'status': 'PROCESSING'}, 'auth': True},
    {'name': '获取待处理订单', 'method': 'GET', 'url': '/api/orders/pending', 'auth': True},
    {'name': '获取处理中订单', 'method': 'GET', 'url': '/api/orders/processing', 'auth': True},
    {'name': '获取所有订单', 'method': 'GET', 'url': '/api/orders', 'auth': True},
    {'name': '按日期范围获取订单', 'method': 'GET', 'url': '/api/orders/date-range?start=2023-01-01&end=2023-12-31', 'auth': True},
]

def login(username, password):
    url = BASE_URL + '/api/auth/login'
    resp = requests.post(url, json={'username': username, 'password': password})
    if resp.status_code == 200:
        token = resp.json().get('token')
        print(f"[登录成功] 用户: {username}")
        return token
    else:
        print(f"[登录失败] 用户: {username}，状态码: {resp.status_code}，响应: {resp.text}")
        return None

def test_endpoints(token):
    headers = {'Authorization': f'Bearer {token}'} if token else {}
    for ep in ENDPOINTS:
        if ep.get('auth') and not token:
            continue
        url = BASE_URL + ep['url']
        method = ep['method'].lower()
        json_data = ep.get('json')
        # 登录接口参数特殊处理
        if ep['url'] == '/api/auth/login' and json_data:
            continue  # 跳过登录接口
        try:
            resp = requests.request(method, url, headers=headers, json=json_data)
            print(f"[{ep['name']}] {ep['method']} {ep['url']} -> 状态码: {resp.status_code}")
            try:
                print('  返回:', resp.json())
            except Exception:
                print('  返回:', resp.text)
        except Exception as e:
            print(f"[{ep['name']}] 请求异常: {e}")

if __name__ == '__main__':
    for user in USERS:
        print(f"\n==== 测试账号: {user['username']} ====")
        token = login(user['username'], user['password'])
        test_endpoints(token) 