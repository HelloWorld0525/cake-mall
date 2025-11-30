<template>
  <el-container class="app-wrapper">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="sidebar-container">
      <AdminAside />
    </el-aside>
    
    <!-- 主体 -->
    <el-container class="main-container">
      <!-- 顶栏 -->
      <el-header height="60px" style="padding: 0;">
        <AdminHeader />
      </el-header>
      
      <!-- 内容区域 -->
      <el-main class="app-main">
        <!-- 加一个淡入淡出过渡动画 -->
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import AdminAside from '../components/AdminAside.vue'
import AdminHeader from '../components/AdminHeader.vue'
</script>

<style scoped>
.app-wrapper {
  width: 100%;
  height: 100vh; /* 强制占满全屏高度 */
  display: flex;
}

.sidebar-container {
  background-color: #001529;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  z-index: 100;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: #f0f2f5; /* 经典的中后台灰色背景 */
}

.app-main {
  flex: 1;
  padding: 20px; /* 内容与边框的间距 */
  overflow-y: auto;
}

/* 简单的过渡动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-10px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(10px);
}
</style>