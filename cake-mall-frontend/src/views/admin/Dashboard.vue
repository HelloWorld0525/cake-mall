<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>工作台</span>
        </div>
      </template>
      <div class="welcome-content">
        <h2>欢迎回来，管理员！</h2>
        <p>今天是 {{ new Date().toLocaleDateString() }}，祝您工作愉快。</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.welcome-card {
  margin-top: 20px;
}
.welcome-content {
  text-align: center;
  padding: 50px 0;
}
h2 {
  color: #409EFF;
}
</style>