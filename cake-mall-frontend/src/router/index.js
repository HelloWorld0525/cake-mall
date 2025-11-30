import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 前台路由
  {
    path: '/',
    component: () => import('../views/FrontLayout.vue'),
    children: [
      {
        path: '', // 空路径表示默认子路由
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      // === 新增商品详情路由 ===
      {
        path: '/goods/:id',
        name: 'GoodsDetail',
        component: () => import('../views/GoodsDetail.vue')
      },
      {
        path: '/cart',
        name: 'Cart',
        component: () => import('../views/Cart.vue')
      },
    // === 新增我的订单路由 ===
      {
        path: '/orders',
        name: 'MyOrders',
        component: () => import('../views/MyOrders.vue')
      },
      // === 新增列表页路由 ===
      { 
        path: '/goods-list', 
        name: 'GoodsList', 
        component: () => import('../views/GoodsList.vue') 
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  // 后台管理路由
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    // 简单的路由守卫：检查是否登录
    beforeEnter: (to, from, next) => {
      const userStr = localStorage.getItem("user");
      if (!userStr) {
        next('/login');
      } else {
        // 这里可以加权限判断
        next();
      }
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue')
      },
      { 
        path: 'user', 
        name: 'UserManage',
        component: () => import('../views/admin/UserManage.vue') 
      },
      { 
        path: 'type', 
        name: 'TypeManage',
        component: () => import('../views/admin/TypeManage.vue') 
      },
      { 
        path: 'goods', 
        name: 'GoodsManage',
        component: () => import('../views/admin/GoodsManage.vue') 
      },
      // === 新增订单管理 ===
      { 
        path: 'order', 
        name: 'OrderManage',
        component: () => import('../views/admin/OrderManage.vue') 
      },
      // === 新增推荐管理 ===
      { 
        path: 'recommend', 
        name: 'RecommendManage',
        component: () => import('../views/admin/RecommendManage.vue') 
      },
      { 
        path: 'order', 
        name: 'OrderManage',
        component: () => import('../views/admin/OrderManage.vue') 
      },
      // ...

    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router