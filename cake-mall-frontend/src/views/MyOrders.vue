<template>
  <div class="my-orders-container">
    <div class="page-header">我的订单</div>

    <div class="order-list" v-loading="loading">
      <el-card v-for="order in orderList" :key="order.id" class="order-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span class="time">{{ order.datetime }}</span>
            <span class="order-no">订单号：{{ order.id }}</span>
            <span class="status-tag">
              <el-tag v-if="order.status === 1" type="success">已支付</el-tag>
              <el-tag v-else type="danger">未支付</el-tag>
            </span>
          </div>
        </template>
        
        <div class="card-body">
          <div class="info-row">
            <div class="info-item">
              <span class="label">收货人：</span>{{ order.name }} ({{ order.phone }})
            </div>
            <div class="info-item">
              <span class="label">总金额：</span>
              <span class="price">￥{{ order.total }}</span>
            </div>
             <div class="info-item">
              <span class="label">地址：</span>{{ order.address }}
            </div>
          </div>
          
          <div class="action-row">
             <el-button type="primary" link @click="viewDetail(order.id)">查看商品详情 >></el-button>
          </div>
        </div>
      </el-card>

      <el-empty v-if="orderList.length === 0" description="您还没有下过单哦" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        layout="prev, pager, next"
        :total="total"
        @current-change="loadData"
      />
    </div>

    <!-- 详情弹窗 (复用后台的逻辑) -->
    <el-dialog v-model="dialogVisible" title="订单商品清单" width="50%">
      <el-table :data="itemList" border stripe>
        <el-table-column label="商品封面" width="100" align="center">
           <template #default="scope">
             <el-image 
               style="width: 60px; height: 60px" 
               :src="scope.row.goodsCover" 
               fit="cover" 
             />
           </template>
        </el-table-column>
        <el-table-column property="goodsName" label="商品名称" />
        <el-table-column property="price" label="单价" width="100">
          <template #default="scope">￥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column property="amount" label="数量" width="80" align="center" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const orderList = ref([])
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const userId = ref(null)

// 详情
const dialogVisible = ref(false)
const itemList = ref([])

const loadData = () => {
  if (!userId.value) return
  
  loading.value = true
  request.get('/order/user/list', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      userId: userId.value
    }
  }).then(res => {
    if (res.code === '200') {
      orderList.value = res.data.records
      total.value = res.data.total
    }
  }).finally(() => {
    loading.value = false
  })
}

const viewDetail = (orderId) => {
  request.get('/order/items/' + orderId).then(res => {
    if (res.code === '200') {
      itemList.value = res.data
      dialogVisible.value = true
    }
  })
}

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    userId.value = JSON.parse(userStr).id
    loadData()
  } else {
    router.push('/login')
  }
})
</script>

<style scoped>
.my-orders-container {
  width: 1000px;
  margin: 20px auto;
  min-height: 500px;
}
.page-header {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  border-left: 5px solid #d35400;
  padding-left: 15px;
}
.order-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.time {
  color: #999;
  font-size: 14px;
}
.order-no {
  color: #666;
  font-size: 14px;
}
.info-row {
  margin-bottom: 15px;
}
.info-item {
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}
.label {
  color: #999;
  display: inline-block;
  width: 70px;
}
.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
.pagination-wrap {
  text-align: center;
  margin-top: 30px;
}
</style>