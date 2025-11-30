<template>
  <div>
    <!-- 轮播图 (Banner) -->
    <div class="banner-section" v-if="bannerList.length > 0">
      <el-carousel height="400px" trigger="click">
        <el-carousel-item v-for="item in bannerList" :key="item.id">
          <div class="banner-item" @click="goToDetail(item.id)">
             <!-- 封面图作为背景或img -->
             <img :src="item.cover" class="banner-img" />
             <div class="banner-text">
               <h3>{{ item.name }}</h3>
               <p>{{ item.intro }}</p>
             </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 热销商品 -->
    <div class="section-header">
      <h2>热销推荐 <span class="sub-title">Hot Sale</span></h2>
    </div>
    <div class="goods-grid">
      <el-card 
        v-for="item in hotList" 
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
            <el-button type="primary" size="small" icon="ShoppingCart" circle @click.stop="addToCart(item)" />
          </div>
        </div>
      </el-card>
      <el-empty v-if="hotList.length === 0" description="暂无热销商品，请在后台添加推荐" />
    </div>

    <!-- 新品推荐 -->
    <div class="section-header">
      <h2>新品上市 <span class="sub-title">New Arrivals</span></h2>
    </div>
    <div class="goods-grid">
      <el-card 
        v-for="item in newList" 
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
            <el-button type="primary" size="small" icon="ShoppingCart" circle @click.stop="addToCart(item)" />
          </div>
        </div>
      </el-card>
       <el-empty v-if="newList.length === 0" description="暂无新品，请在后台添加推荐" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const bannerList = ref([])
const hotList = ref([])
const newList = ref([])

const loadData = () => {
  // 获取轮播图 (Type=3)
  request.get('/recommend/goods', { params: { type: 3 } }).then(res => {
    bannerList.value = res.data || []
  })
  // 获取热销 (Type=1)
  request.get('/recommend/goods', { params: { type: 1 } }).then(res => {
    hotList.value = res.data || []
  })
  // 获取新品 (Type=2)
  request.get('/recommend/goods', { params: { type: 2 } }).then(res => {
    newList.value = res.data || []
  })
}

const goToDetail = (id) => {
    router.push('/goods/' + id)
}

const addToCart = (item) => {
  ElMessage.success('已加入购物车: ' + item.name)
  // TODO: 调用加入购物车接口
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}
.section-header {
  margin: 40px 0 20px;
  border-left: 5px solid #d35400;
  padding-left: 15px;
}
.section-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}
.sub-title {
  font-size: 14px;
  color: #999;
  margin-left: 10px;
  font-weight: normal;
}
.goods-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4列 */
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
</style>