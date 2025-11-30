<template>
  <div class="goods-list-container">
    <!-- 面包屑 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ title }}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 标题 -->
    <div class="page-title">{{ title }}</div>

    <!-- 商品列表 -->
    <div class="goods-grid" v-loading="loading">
      <el-card 
        v-for="item in list" 
        :key="item.id" 
        class="goods-card" 
        :body-style="{ padding: '0px' }"
        shadow="hover"
        @click="goToDetail(item.id)"
      >
        <img :src="item.cover" class="goods-image"/>
        <div style="padding: 14px;">
          <div class="goods-name">{{ item.name }}</div>
          <div class="goods-price">
            <span>￥{{ item.price }}</span>
            <!-- 简单的加购按钮 -->
            <el-button type="primary" size="small" icon="ShoppingCart" circle @click.stop="addToCart(item)" />
          </div>
        </div>
      </el-card>
    </div>
    
    <el-empty v-if="!loading && list.length === 0" description="暂无相关商品" />

    <!-- 分页 (仅在非推荐模式下显示，因为推荐接口目前没做分页) -->
    <div class="pagination-wrap" v-if="total > 0 && !isRecommendMode">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        layout="prev, pager, next"
        :total="total"
        @current-change="fetchData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const list = ref([])
const pageNum = ref(1)
const pageSize = ref(12) // 每页显示12个
const total = ref(0)
const isRecommendMode = ref(false)

// 动态计算页面标题
const title = computed(() => {
  if (route.query.keyword) return `搜索: "${route.query.keyword}"`
  if (route.query.typeId) return '分类浏览' // 这里如果能拿到分类名更好，简化处理暂显示统称
  if (route.query.recommendType == 1) return '热销商品'
  if (route.query.recommendType == 2) return '新品推荐'
  return '商品列表'
})

// 核心数据加载逻辑
const fetchData = () => {
  loading.value = true
  list.value = []
  
  const keyword = route.query.keyword
  const typeId = route.query.typeId
  const recommendType = route.query.recommendType

  // 场景1: 热销或新品 (调用 recommend 接口)
  if (recommendType) {
    isRecommendMode.value = true
    request.get('/recommend/goods', {
      params: { type: recommendType }
    }).then(res => {
      list.value = res.data || []
      total.value = 0 // 推荐接口未分页
    }).finally(() => loading.value = false)
    return
  }

  // 场景2: 搜索 或 分类筛选 (调用 goods/list 接口)
  isRecommendMode.value = false
  request.get('/goods/list', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: keyword,
      typeId: typeId
    }
  }).then(res => {
    if (res.code === '200') {
      list.value = res.data.records
      total.value = res.data.total
    }
  }).finally(() => loading.value = false)
}

// 监听路由参数变化（例如从搜索A变更为搜索B，或者切换分类），重新加载数据
watch(() => route.query, () => {
  pageNum.value = 1
  fetchData()
}, { immediate: true })

const goToDetail = (id) => {
  router.push('/goods/' + id)
}

// 复用加购逻辑
const addToCart = (goods) => {
  let cart = JSON.parse(localStorage.getItem('cart') || '[]')
  const existItem = cart.find(item => item.id === goods.id)
  if (existItem) {
    existItem.count += 1
  } else {
    cart.push({
      id: goods.id,
      name: goods.name,
      cover: goods.cover,
      price: goods.price,
      count: 1
    })
  }
  localStorage.setItem('cart', JSON.stringify(cart))
  ElMessage.success('已加入购物车')
}
</script>

<style scoped>
.goods-list-container {
  width: 1200px;
  margin: 20px auto;
  min-height: 500px;
}
.breadcrumb {
  margin-bottom: 20px;
}
.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
  border-left: 5px solid #d35400;
  padding-left: 15px;
}
.goods-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.goods-card {
  cursor: pointer;
  transition: all 0.3s;
}
.goods-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}
.goods-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}
.goods-name {
  font-size: 16px;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.goods-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-wrap {
  text-align: center;
  margin-top: 40px;
}
</style>