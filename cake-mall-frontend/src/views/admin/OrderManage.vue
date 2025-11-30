<template>
  <div>
    <!-- 标题 -->
    <div style="margin-bottom: 20px; font-weight: bold; font-size: 16px; color: #333;">
      订单管理列表
    </div>

    <!-- 订单列表表格 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="total" label="总金额" width="100">
        <template #default="scope">￥{{ scope.row.total }}</template>
      </el-table-column>
      <el-table-column prop="amount" label="商品总数" width="100" align="center" />
      
      <!-- 支付状态 -->
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="getPaymentStatusType(scope.row.status)">已支付</el-tag>
          <el-tag v-else type="danger">未支付</el-tag>
          <!-- 这里可以扩展更多状态，如发货、完成等 -->
        </template>
      </el-table-column>
      
      <!-- 支付方式 -->
      <el-table-column prop="paytype" label="支付方式" width="120" align="center">
        <template #default="scope">
          <span v-if="scope.row.paytype === 1">微信</span>
          <span v-else-if="scope.row.paytype === 2">支付宝</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="收货人" width="120" />
      <el-table-column prop="phone" label="联系电话" width="120" />
      <el-table-column prop="address" label="收货地址" show-overflow-tooltip />
      <el-table-column prop="datetime" label="下单时间" width="180" />

      <!-- 操作列 -->
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="viewItems(scope.row.id)">查看详情</el-button>
          <el-popconfirm title="确定删除该订单吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="itemDialogVisible" title="订单详情" width="50%">
      <el-table :data="itemList" border stripe>
        <el-table-column label="商品封面" width="100" align="center">
           <template #default="scope">
             <el-image 
               style="width: 50px; height: 50px" 
               :src="scope.row.goodsCover" 
               fit="cover" 
             />
           </template>
        </el-table-column>
        <el-table-column property="goodsName" label="商品名称" />
        <el-table-column property="price" label="购买单价" width="120">
          <template #default="scope">￥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column property="amount" label="购买数量" width="100" align="center" />
        <el-table-column label="小计" width="120">
           <template #default="scope">
             ￥{{ (scope.row.price * scope.row.amount).toFixed(1) }}
           </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="itemDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 详情弹窗相关
const itemDialogVisible = ref(false)
const itemList = ref([])

// 加载订单列表
const loadData = () => {
  loading.value = true
  request.get('/order/list', {
    params: { pageNum: pageNum.value, pageSize: pageSize.value }
  }).then(res => {
    if (res.code === '200') {
      tableData.value = res.data.records
      total.value = res.data.total
    }
  }).finally(() => {
    loading.value = false
  })
}

// 查看详情
const viewItems = (orderId) => {
  request.get('/order/items/' + orderId).then(res => {
    if (res.code === '200') {
      itemList.value = res.data
      itemDialogVisible.value = true
    } else {
      ElMessage.error('获取详情失败')
    }
  })
}

// 删除订单
const handleDelete = (id) => {
  request.delete('/order/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  loadData()
})
</script>