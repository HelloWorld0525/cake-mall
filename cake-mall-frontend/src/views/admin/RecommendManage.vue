<template>
  <div>
    <div style="margin-bottom: 20px; font-weight: bold;">营销推荐管理</div>
    
    <el-tabs v-model="activeType" type="card" @tab-change="handleTabChange">
      <el-tab-pane label="热销推荐" :name="1"></el-tab-pane>
      <el-tab-pane label="新品推荐" :name="2"></el-tab-pane>
      <el-tab-pane label="轮播图推荐" :name="3"></el-tab-pane>
    </el-tabs>

    <!-- 操作栏 -->
    <div style="margin: 10px 0;">
      <el-button type="primary" @click="openAddDialog">添加商品到{{ typeName }}</el-button>
    </div>

    <!-- 已推荐列表 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column label="推荐ID" prop="id" width="80" align="center" />
      <el-table-column label="商品图片" width="100" align="center">
        <template #default="scope">
          <el-image :src="scope.row.cover" style="width: 50px; height: 50px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="name" />
      <el-table-column label="价格" prop="price" width="100">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-popconfirm title="确定移出推荐吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">移出</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 推荐列表分页 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        layout="prev, pager, next"
        :total="total"
        @current-change="loadData"
      />
    </div>

    <!-- ================= 添加商品弹窗 ================= -->
    <el-dialog v-model="dialogVisible" title="选择商品加入推荐" width="60%">
      <div style="margin-bottom: 15px;">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索商品名称" 
          style="width: 200px; margin-right: 10px;" 
          clearable 
          @clear="loadGoods"
        />
        <el-button type="primary" @click="loadGoods">查询</el-button>
      </div>
      
      <el-table :data="goodsList" border stripe height="400" @row-click="selectGoods">
        <el-table-column label="ID" prop="id" width="60" />
        <el-table-column label="图片" width="80">
          <template #default="scope">
            <el-image :src="scope.row.cover" style="width: 40px; height: 40px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name" />
        <el-table-column label="价格" prop="price" width="100" />
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
             <el-button type="success" size="small" @click.stop="addRecommend(scope.row.id)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px; text-align: right;">
        <el-pagination
          v-model:current-page="goodsPageNum"
          v-model:page-size="goodsPageSize"
          layout="prev, pager, next"
          :total="goodsTotal"
          @current-change="loadGoods"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const activeType = ref(1) // 1热销 2新品 3轮播
const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 弹窗相关
const dialogVisible = ref(false)
const goodsList = ref([])
const searchKeyword = ref('')
const goodsPageNum = ref(1)
const goodsPageSize = ref(10)
const goodsTotal = ref(0)

const typeName = computed(() => {
  if (activeType.value === 1) return '热销'
  if (activeType.value === 2) return '新品'
  if (activeType.value === 3) return '轮播图'
  return ''
})

// 加载已推荐列表
const loadData = () => {
  loading.value = true
  request.get('/recommend/page', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      type: activeType.value
    }
  }).then(res => {
    if (res.code === '200') {
      tableData.value = res.data.records
      total.value = res.data.total
    }
  }).finally(() => {
    loading.value = false
  })
}

const handleTabChange = () => {
  pageNum.value = 1
  loadData()
}

// 移除推荐
const handleDelete = (id) => {
  request.delete('/recommend/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('移除成功')
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// === 添加相关逻辑 ===

const openAddDialog = () => {
  dialogVisible.value = true
  loadGoods()
}

// 加载所有商品 (用于选择)
const loadGoods = () => {
  request.get('/goods/list', {
    params: {
      pageNum: goodsPageNum.value,
      pageSize: goodsPageSize.value,
      name: searchKeyword.value
    }
  }).then(res => {
    if (res.code === '200') {
      goodsList.value = res.data.records
      goodsTotal.value = res.data.total
    }
  })
}

const addRecommend = (goodsId) => {
  request.post('/recommend/add', {
    type: activeType.value,
    goodsId: goodsId
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      loadData() // 刷新列表
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  loadData()
})
</script>