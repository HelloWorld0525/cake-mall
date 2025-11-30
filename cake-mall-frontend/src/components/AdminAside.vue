<template>
  <div class="aside-container">
    <!-- Logo 区域 -->
    <div class="logo-wrapper">
      <el-icon :size="24" color="#d35400" class="logo-icon"><Monitor /></el-icon>
      <span class="logo-text">蛋糕商城管理</span>
    </div>

    <!-- 菜单区域 -->
    <el-menu
      :default-active="route.path"
      background-color="#001529"
      text-color="rgba(255, 255, 255, 0.65)"
      active-text-color="#fff"
      router
      :unique-opened="true"
      class="el-menu-vertical"
    >
      <el-menu-item index="/admin/dashboard">
        <el-icon><Odometer /></el-icon>
        <span>控制台</span>
      </el-menu-item>

      <el-sub-menu index="1">
        <template #title>
          <el-icon><Goods /></el-icon>
          <span>商品中心</span>
        </template>
        <el-menu-item index="/admin/goods">商品管理</el-menu-item>
        <el-menu-item index="/admin/type">类目管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/admin/order">
        <el-icon><List /></el-icon>
        <span>订单中心</span>
      </el-menu-item>

      <el-menu-item index="/admin/user">
        <el-icon><User /></el-icon>
        <span>客户管理</span>
      </el-menu-item>

      <el-menu-item index="/admin/recommend">
        <el-icon><Star /></el-icon>
        <span>营销推广</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { Monitor, Odometer, Goods, List, User, Star } from '@element-plus/icons-vue'
const route = useRoute()
</script>

<style scoped>
.aside-container {
  height: 100%;
  background-color: #001529;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  flex-direction: column;
}

.logo-wrapper {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #002140; /* 比侧边栏稍深，区分Logo区 */
  overflow: hidden;
  transition: all 0.3s;
}

.logo-icon {
  margin-right: 10px;
}

.logo-text {
  color: #fff;
  font-weight: 600;
  font-size: 18px;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  white-space: nowrap;
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
}

/* 选中项的高亮样式优化 */
:deep(.el-menu-item.is-active) {
  background-color: #d35400 !important; /* 品牌主色 */
  color: #fff;
}

:deep(.el-menu-item:hover) {
  color: #fff !important;
}
</style>