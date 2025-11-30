<template>
  <div class="login-container">
    <div class="overlay"></div>
    
    <div class="login-card">
      <div class="brand-header">
        <h2 class="title">Join Cake Mall</h2>
        <p class="subtitle">开启您的甜蜜之旅</p>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" size="large" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="设置用户名" :prefix-icon="User" />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="设置密码" :prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="绑定邮箱" :prefix-icon="Message" />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister" 
            class="submit-btn" 
            :loading="loading"
            round
          >
            立即注册
          </el-button>
        </el-form-item>

        <div class="form-footer">
          <span>已有账号？</span>
          <el-link type="primary" :underline="false" @click="router.push('/login')">立即登录</el-link>
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
import { User, Lock, Message } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  email: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
}

const handleRegister = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      request.post('/user/register', form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        } else {
          ElMessage.error(res.msg || '注册失败')
        }
      }).finally(() => {
        loading.value = false
      })
    }
  })
}
</script>

<style scoped>
/* 复用登录页样式，保持一致性 */
.login-container {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 换一张不同的甜点背景图 */
  background: url('https://ix-marketing.imgix.net/autocompress.png?auto=format,compress&w=1946') no-repeat center center;
  background-size: cover;
  position: relative;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(4px);
}

.login-card {
  position: relative;
  width: 420px;
  padding: 40px 40px;
  background: rgba(255, 255, 255, 0.96);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.brand-header {
  margin-bottom: 30px;
}

.title {
  font-size: 26px;
  color: #2c3e50;
  margin: 10px 0 5px;
  font-family: 'Georgia', serif;
}

.subtitle {
  font-size: 14px;
  color: #909399;
  letter-spacing: 1px;
}

.login-form {
  margin-top: 20px;
}

.submit-btn {
  width: 100%;
  padding: 20px 0;
  font-size: 16px;
  letter-spacing: 2px;
  margin-top: 10px;
  box-shadow: 0 4px 12px rgba(211, 84, 0, 0.3);
}

.form-footer {
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>