<template>
  <div class="front-wrapper">
    <!-- 顶部导航栏 -->
    <div class="header-shadow">
      <div class="header-inner">
        <!-- Logo -->
        <div class="logo-area" @click="router.push('/')">
          <div class="logo-icon-bg">
             <el-icon :size="24" color="#fff"><Food /></el-icon>
          </div>
          <span class="logo-text">Cake Mall</span>
        </div>
        
        <!-- 中间导航 -->
        <div class="nav-menu">
          <div class="nav-item" :class="{ active: route.path === '/' }" @click="router.push('/')">首页</div>
          
          <el-dropdown class="nav-item-dropdown">
            <span class="nav-item">
              全部分类 <el-icon class="icon-right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="custom-dropdown">
                <el-dropdown-item @click="router.push('/goods-list')">全部商品</el-dropdown-item>
                <el-dropdown-item 
                  v-for="type in typeList" 
                  :key="type.id" 
                  @click="searchByType(type.id)"
                >
                  {{ type.name }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <div class="nav-item" @click="router.push('/goods-list?recommendType=1')">热销榜单</div>
          <div class="nav-item" @click="router.push('/goods-list?recommendType=2')">新品首发</div>
        </div>

        <!-- 右侧搜索与用户 -->
        <div class="right-actions">
          <div class="search-box">
             <el-input 
               v-model="keyword" 
               placeholder="搜索美味..." 
               class="search-input"
               @keyup.enter="handleSearch"
             >
               <template #suffix>
                 <el-icon class="search-icon" @click="handleSearch"><Search /></el-icon>
               </template>
             </el-input>
          </div>

          <!-- 购物车 -->
          <div class="action-btn cart-btn" @click="router.push('/cart')">
             <el-badge :value="cartCount" :max="99" :hidden="cartCount === 0" class="badge-custom">
               <el-icon :size="22"><ShoppingCartFull /></el-icon>
             </el-badge>
             <span class="btn-text">购物车</span>
          </div>

          <!-- 用户 -->
          <div class="user-area">
            <template v-if="user.id">
              <el-dropdown>
                <div class="avatar-link">
                   <el-avatar :size="32" style="background-color: #fde3cf; color: #f56a00;">{{ user.username.charAt(0).toUpperCase() }}</el-avatar>
                   <span class="username-text">{{ user.username }}</span>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="router.push('/admin')" v-if="user.isadmin">后台管理</el-dropdown-item>
                    <el-dropdown-item @click="router.push('/orders')">我的订单</el-dropdown-item>
                    <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <div class="auth-btns">
                <el-button type="primary" link @click="router.push('/login')">登录</el-button>
                <el-divider direction="vertical" />
                <el-button type="primary" link @click="router.push('/register')">注册</el-button>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="main-content">
      <router-view v-slot="{ Component }">
         <transition name="fade" mode="out-in">
            <component :is="Component" />
         </transition>
      </router-view>
    </div>

    <!-- 底部 -->
    <div class="footer">
      <div class="footer-content">
         <div class="footer-links">
           <span>关于我们</span>
           <span>联系客服</span>
           <span>配送服务</span>
           <span>商务合作</span>
         </div>
         <p class="copyright">Copyright © 2023 Cake Mall Inc. All Rights Reserved.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '../utils/request'
import { Food, ShoppingCartFull, Search, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const keyword = ref('')
const typeList = ref([])
const user = reactive(JSON.parse(localStorage.getItem('user') || '{}'))
const cartCount = ref(0)

const updateCartCount = () => {
  const cart = JSON.parse(localStorage.getItem('cart') || '[]')
  cartCount.value = cart.length
}

const loadTypes = () => {
  request.get('/type/findAll').then(res => {
    if (res.code === '200') typeList.value = res.data
  })
}

const searchByType = (id) => router.push(`/goods-list?typeId=${id}`)
const handleSearch = () => {
  router.push(keyword.value ? `/goods-list?keyword=${keyword.value}` : `/goods-list`)
}
const logout = () => {
  localStorage.removeItem('user')
  window.location.href = '/'
}

onMounted(() => {
  loadTypes()
  updateCartCount()
})
</script>

<style scoped>
.front-wrapper {
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Header Styles */
.header-shadow {
  background: #fff;
  box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 999;
}

.header-inner {
  max-width: 1280px;
  margin: 0 auto;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

/* Logo */
.logo-area {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.logo-icon-bg {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #ff9c6e 0%, #d35400 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  box-shadow: 0 4px 10px rgba(211, 84, 0, 0.3);
}
.logo-text {
  font-size: 22px;
  font-weight: 800;
  color: #2c3e50;
  font-family: 'Avenir', sans-serif;
  letter-spacing: -0.5px;
}

/* Navigation */
.nav-menu {
  display: flex;
  gap: 30px;
  margin-left: 40px;
  flex: 1;
}
.nav-item {
  font-size: 16px;
  color: #505050;
  cursor: pointer;
  font-weight: 500;
  padding: 8px 0;
  position: relative;
  transition: color 0.3s;
}
.nav-item:hover, .nav-item.active {
  color: #d35400;
}
/* 简单的下划线动画 */
.nav-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: #d35400;
  transition: width 0.3s;
}
.nav-item:hover::after, .nav-item.active::after {
  width: 100%;
}
.nav-item-dropdown {
  display: flex;
  align-items: center;
}

/* Right Actions */
.right-actions {
  display: flex;
  align-items: center;
  gap: 25px;
}

.search-box .search-input {
  width: 220px;
  transition: width 0.3s;
}
.search-box .search-input:focus-within {
  width: 280px;
}
:deep(.el-input__wrapper) {
  border-radius: 20px;
  background-color: #f7f9fa;
  box-shadow: none !important;
  border: 1px solid transparent;
}
:deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: #d35400;
  box-shadow: 0 0 0 1px #d35400 !important;
}

.action-btn {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
}
.action-btn:hover {
  color: #d35400;
}
.btn-text {
  margin-left: 6px;
  font-size: 14px;
}

.avatar-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}
.username-text {
  font-weight: 500;
  color: #333;
}

/* Main Content */
.main-content {
  flex: 1;
  width: 100%;
  max-width: 1280px;
  margin: 30px auto;
  padding: 0 20px;
}

/* Footer */
.footer {
  background: #232f3e;
  color: #fff;
  padding: 40px 0;
  margin-top: auto;
}
.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}
.footer-links {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #ccc;
}
.footer-links span {
  cursor: pointer;
}
.footer-links span:hover {
  color: #fff;
}
.copyright {
  font-size: 12px;
  color: #666;
}

/* Transition */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>