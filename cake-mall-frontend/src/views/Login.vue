<template>
  <div class="login-container">
    <!-- 背景遮罩，让文字更清晰 -->
    <div class="overlay"></div>
    
    <div class="login-card">
      <div class="brand-header">
        <el-icon :size="40" color="#d35400"><Food /></el-icon>
        <h2 class="title">Cake Mall</h2>
        <p class="subtitle">让甜蜜触手可及</p>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" size="large" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名 / 手机号" 
            :prefix-icon="User" 
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码" 
            :prefix-icon="Lock" 
            show-password 
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin" 
            class="submit-btn" 
            :loading="loading"
            round
          >
            登 录
          </el-button>
        </el-form-item>

        <div class="form-footer">
          <span>还没有账号？</span>
          <el-link type="primary" :underline="false" @click="router.push('/register')">立即注册</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { User, Lock, Food } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      request.post('/user/login', form).then(res => {
        if (res.code === '200') {
          ElMessage.success('欢迎回来')
          const rawData = res.data;
          const userSave = { 
            ...rawData.user, 
            token: rawData.token 
          };
          localStorage.setItem("user", JSON.stringify(userSave))
          
          if (userSave.isadmin) {
            router.push('/admin')
          } else {
            router.push('/')
          }
        } else {
          ElMessage.error(res.msg || '登录失败')
        }
      }).catch(err => {
        console.error(err)
      }).finally(() => {
        loading.value = false
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 背景图：这里使用 Unsplash 的免费图床，您也可以换成 files 目录下的本地图片 */
  background: url('https://images.unsplash.com/photo-1578985545062-69928b1d9587?q=80&w=2089&auto=format&fit=crop') no-repeat center center;
  background-size: cover;
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4); /* 黑色半透明遮罩 */
  backdrop-filter: blur(4px); /* 背景模糊效果 */
}

.login-card {
  position: relative; /* 确保在遮罩之上 */
  width: 420px;
  padding: 50px 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  text-align: center;
  transition: transform 0.3s;
}

.login-card:hover {
  transform: translateY(-5px);
}

.brand-header {
  margin-bottom: 40px;
}

.title {
  font-size: 28px;
  color: #2c3e50;
  margin: 10px 0 5px;
  font-family: 'Georgia', serif; /* 衬线体更有高级感 */
}

.subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
  letter-spacing: 2px;
}

.login-form {
  margin-top: 20px;
}

.submit-btn {
  width: 100%;
  padding: 20px 0;
  font-size: 16px;
  letter-spacing: 4px;
  margin-top: 10px;
  box-shadow: 0 4px 12px rgba(211, 84, 0, 0.3);
}

.form-footer {
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>