<template>
  <div class="goods-detail-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品详情</el-breadcrumb-item>
      <el-breadcrumb-item>{{ goods.name }}</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="main-info" v-loading="loading">
      <!-- 左侧图片展示 -->
      <div class="image-gallery">
        <el-image :src="currentImg" fit="cover" class="main-img" :preview-src-list="previewList" />
        <div class="thumbnail-list">
          <div 
            v-for="(img, index) in imgList" 
            :key="index" 
            class="thumbnail-item" 
            :class="{ active: currentImg === img }"
            @click="currentImg = img"
          >
            <el-image :src="img" fit="cover" class="thumb-img" />
          </div>
        </div>
      </div>

      <!-- 右侧信息 -->
      <div class="info-wrap">
        <h1 class="title">{{ goods.name }}</h1>
        <div class="intro">{{ goods.intro || '暂无描述' }}</div>
        
        <div class="price-box">
          <span class="label">价格</span>
          <span class="price">￥{{ goods.price }}</span>
        </div>

        <div class="meta-row">
          <span class="label">库存</span>
          <span class="stock">{{ goods.stock }} 件</span>
        </div>

        <div class="meta-row" style="margin-top: 20px;">
          <span class="label">数量</span>
          <el-input-number v-model="buyCount" :min="1" :max="goods.stock" />
        </div>

        <div class="action-bar">
          <el-button type="warning" size="large" icon="ShoppingCart" @click="addToCart">加入购物车</el-button>
          <el-button type="danger" size="large" @click="buyNow">立即购买</el-button>
        </div>
      </div>
    </div>

    <!-- 商品详情 (图文介绍) -->
    <div class="detail-section">
      <div class="section-title">商品详情</div>
      <div class="content">
        <!-- 如果数据库里 intro 是纯文本，就简单显示；如果是富文本可以用 v-html -->
        <p>{{ goods.intro }}</p>
        <div v-if="goods.image1" class="detail-img-wrap">
          <img :src="goods.image1" />
        </div>
        <div v-if="goods.image2" class="detail-img-wrap">
          <img :src="goods.image2" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const goodsId = route.params.id
const loading = ref(false)
const buyCount = ref(1)

const goods = reactive({})
const currentImg = ref('')

// 构造图片列表，方便切换和预览
const imgList = computed(() => {
  const list = []
  if (goods.cover) list.push(goods.cover)
  if (goods.image1) list.push(goods.image1)
  if (goods.image2) list.push(goods.image2)
  return list
})
const previewList = computed(() => imgList.value)

// 加载商品详情
const loadData = () => {
  loading.value = true
  request.get('/goods/' + goodsId).then(res => {
    if (res.code === '200' && res.data) {
      Object.assign(goods, res.data)
      currentImg.value = goods.cover // 默认显示封面
    } else {
      ElMessage.error('商品不存在或已下架')
      setTimeout(() => router.push('/'), 1500)
    }
  }).finally(() => {
    loading.value = false
  })
}

// 加入购物车逻辑 (LocalStorage)
const addToCart = () => {
  // 1. 获取现有购物车
  let cart = JSON.parse(localStorage.getItem('cart') || '[]')
  
  // 2. 检查是否已存在该商品
  const existItem = cart.find(item => item.id === goods.id)
  if (existItem) {
    existItem.count += buyCount.value
  } else {
    cart.push({
      id: goods.id,
      name: goods.name,
      cover: goods.cover,
      price: goods.price,
      count: buyCount.value
    })
  }
  
  // 3. 保存回 LocalStorage
  localStorage.setItem('cart', JSON.stringify(cart))
  ElMessage.success('已加入购物车')
  
  // TODO: 这里可以触发一个事件通知 Header 更新购物车角标
}

// 立即购买
const buyNow = () => {
  // 检查登录状态
  const user = localStorage.getItem('user')
  if (!user) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  // 既然是立即购买，我们可以通过路由传参直接去订单结算页(下一章开发)，或者简单处理：加入购物车并跳转
  addToCart()
  router.push('/cart')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.goods-detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  min-height: 500px;
}
.breadcrumb {
  margin-bottom: 20px;
}
.main-info {
  display: flex;
  gap: 40px;
}
.image-gallery {
  width: 400px;
}
.main-img {
  width: 100%;
  height: 400px;
  border: 1px solid #eee;
  border-radius: 4px;
}
.thumbnail-list {
  display: flex;
  margin-top: 10px;
  gap: 10px;
}
.thumbnail-item {
  width: 60px;
  height: 60px;
  border: 2px solid transparent;
  cursor: pointer;
}
.thumbnail-item.active {
  border-color: #d35400;
}
.thumb-img {
  width: 100%;
  height: 100%;
}

.info-wrap {
  flex: 1;
}
.title {
  font-size: 24px;
  margin-top: 0;
  color: #333;
}
.intro {
  color: #999;
  margin-bottom: 20px;
}
.price-box {
  background: #fdf5f2;
  padding: 15px;
  margin-bottom: 20px;
}
.price-box .label {
  color: #999;
  margin-right: 20px;
}
.price-box .price {
  color: #e4393c;
  font-size: 28px;
  font-weight: bold;
}
.meta-row {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}
.meta-row .label {
  color: #999;
  width: 60px;
}
.action-bar {
  margin-top: 40px;
  display: flex;
  gap: 20px;
}

.detail-section {
  margin-top: 50px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  padding-left: 10px;
  border-left: 4px solid #d35400;
}
.detail-img-wrap img {
  max-width: 100%;
  display: block;
  margin: 10px auto;
}
</style>