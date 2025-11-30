# 🍰 网上蛋糕商城 (Cake Mall)

基于 Spring Boot 3 + Vue 3 + Element Plus 的全栈 B2C 电商项目。

## ✨ 项目简介

这是一个前后端分离的蛋糕销售商城，包含用户端（商品浏览、购物车、下单支付）和管理端（商品管理、订单管理、用户管理、营销管理）。

## 🛠️ 技术栈

**后端 (Backend):**

- Java 17
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.0
- JWT (鉴权)

**前端 (Frontend):**

- Vue 3 (Composition API)
- Vite
- Element Plus
- Axios
- Vue Router

## 🚀 快速开始

### 1. 环境准备

- JDK 17+
- Node.js 16+
- MySQL 8.0+

### 2. 数据库设置

1. 创建数据库 `cake_mall`。
2. 运行 `backend/src/main/resources/init_schema.sql` 脚本初始化表结构。
3. 修改 `backend/src/main/resources/application.yml` 中的数据库密码。

### 3. 后端启动

```
cd backend
mvn spring-boot:run
```

服务默认运行在 `http://localhost:8080`

### 4. 前端启动

```
cd frontend
npm install
npm run dev
```

访问地址：`http://localhost:5173`

## 📂 目录结构

- `backend/`: 后端工程源码
- `frontend/`: 前端工程源码
- `files/`: 本地图片存储目录

## 📸 截图展示

![登录页面](C:\Users\28239\Pictures\Screenshots\pixpin\登录页面.png)

![首页](C:\Users\28239\Pictures\Screenshots\pixpin\首页.png)

![购物车](C:\Users\28239\Pictures\Screenshots\pixpin\购物车.png)

![后台页面](C:\Users\28239\Pictures\Screenshots\pixpin\后台页面.png)
