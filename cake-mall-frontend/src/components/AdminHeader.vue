<template>
  <div class="header-container">
    <div class="left-panel">
      <!-- 简单的面包屑，暂静态，可根据路由动态生成 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>管理系统</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="right-panel">
      <!-- 全屏/消息通知图标预留 -->
      <el-icon class="action-icon" :size="20"><Bell /></el-icon>
      
      <!-- 用户下拉 -->
      <el-dropdown trigger="click">
        <div class="avatar-wrapper">
          <!-- 这里的src可以是用户头像，没有就用默认 -->
          <el-avatar :size="30" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <span class="username">{{ user.username || 'Admin' }}</span>
          <el-icon><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="router.push('/')">返回前台</el-dropdown-item>
            <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Bell, CaretBottom } from '@element-plus/icons-vue'

const router = useRouter()
const user = reactive(JSON.parse(localStorage.getItem('user') || '{}'))

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.header-container {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08); /* 轻微阴影，增加层次感 */
  position: relative;
  z-index: 10;
}

.left-panel {
  display: flex;
  align-items: center;
}

.right-panel {
  display: flex;
  align-items: center;
}

.action-icon {
  margin-right: 20px;
  cursor: pointer;
  color: #5a5e66;
  transition: color 0.3s;
}
.action-icon:hover {
  color: #d35400;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #5a5e66;
}

.username {
  margin: 0 5px 0 10px;
  font-weight: 500;
  font-size: 14px;
}
</style>