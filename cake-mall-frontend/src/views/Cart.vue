<template>
  <div class="cart-container">
    <div class="section-title">我的购物车</div>
    
    <el-table :data="cartList" border stripe style="width: 100%" empty-text="购物车空空如也，快去选购吧">
      <el-table-column label="商品信息" min-width="300">
        <template #default="scope">
          <div class="goods-info">
            <img :src="scope.row.cover" class="goods-img"/>
            <span class="goods-name">{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="price" label="单价" width="120" align="center">
        <template #default="scope">￥{{ scope.row.price }}</template>
      </el-table-column>
      
      <el-table-column label="数量" width="200" align="center">
        <template #default="scope">
          <el-input-number 
            v-model="scope.row.count" 
            :min="1" 
            size="small" 
            @change="updateCart"
          />
        </template>
      </el-table-column>
      
      <el-table-column label="小计" width="120" align="center">
        <template #default="scope">
          <!-- 修复点1: 金额保留两位小数 -->
          <span style="color: #f56c6c; font-weight: bold;">
            ￥{{ (scope.row.price * scope.row.count).toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <el-button type="danger" icon="Delete" circle @click="removeItem(scope.$index)" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部结算栏 -->
    <div class="cart-footer" v-if="cartList.length > 0">
      <div class="left">
        <el-button type="info" text @click="clearCart">清空购物车</el-button>
      </div>
      <div class="right">
        <!-- 修复点1: 总计保留两位小数 -->
        <span class="total-text">总计: <span class="total-price">￥{{ totalPrice }}</span></span>
        <el-button type="primary" size="large" @click="openCheckout">去结算</el-button>
      </div>
    </div>

    <!-- 结算对话框 -->
    <el-dialog v-model="dialogVisible" title="填写订单信息" width="40%">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="收货人" prop="name">
          <el-input v-model="form.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入详细收货地址" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paytype">
          <el-radio-group v-model="form.paytype">
            <el-radio :label="1">微信支付</el-radio>
            <el-radio :label="2">支付宝</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrder" :loading="submitting">确认支付</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const cartList = ref([])
const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  name: '',
  phone: '',
  address: '',
  paytype: 1
})

const rules = {
  name: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  paytype: [{ required: true, message: '请选择支付方式', trigger: 'change' }]
}

// 修复点1: 计算总价保留两位小数
const totalPrice = computed(() => {
  let sum = 0
  cartList.value.forEach(item => {
    sum += item.price * item.count
  })
  return sum.toFixed(2)
})

const loadCart = () => {
  const json = localStorage.getItem('cart')
  cartList.value = json ? JSON.parse(json) : []
}

const updateCart = () => {
  localStorage.setItem('cart', JSON.stringify(cartList.value))
}

const removeItem = (index) => {
  cartList.value.splice(index, 1)
  updateCart()
  ElMessage.success('已移除')
}

const clearCart = () => {
  ElMessageBox.confirm('确定要清空购物车吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    cartList.value = []
    updateCart()
  })
}

// 修复点2: 更严谨的登录检查逻辑
const openCheckout = () => {
  const userStr = localStorage.getItem('user')
  // 如果没有 user 字符串，或者 user 字符串是空的，或者解析后没有 ID
  if (!userStr) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    const userInfo = JSON.parse(userStr)
    if (!userInfo || !userInfo.id) {
      ElMessage.warning('登录信息已过期，请重新登录')
      // 清理掉无效的 user 信息
      localStorage.removeItem('user')
      router.push('/login')
      return
    }

    // 回填信息
    if (userInfo.address) form.address = userInfo.address
    if (userInfo.phone) form.phone = userInfo.phone
    if (userInfo.name) form.name = userInfo.name
    
    dialogVisible.value = true
  } catch (e) {
    localStorage.removeItem('user')
    router.push('/login')
  }
}

// 提交订单
const submitOrder = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      // 再次检查用户ID，防止弹窗打开后 localStorage 被清空等极端情况
      const userStr = localStorage.getItem('user')
      let userId = null;
      try {
        userId = JSON.parse(userStr).id;
      } catch(e) {
         ElMessage.error('用户状态异常，请重新登录');
         router.push('/login');
         return;
      }

      submitting.value = true
      
      const orderDto = {
        // 修复点3: 明确转换为浮点数，防止传字符串给后端
        total: parseFloat(totalPrice.value),
        amount: cartList.value.reduce((sum, item) => sum + item.count, 0),
        paytype: form.paytype,
        name: form.name,
        phone: form.phone,
        address: form.address,
        userId: userId, // 使用明确获取的 ID
        items: cartList.value.map(item => ({
          goodsId: item.id,
          price: item.price,
          amount: item.count
        }))
      }

      request.post('/order/add', orderDto).then(res => {
        if (res.code === '200') {
          ElMessage.success('支付成功！我们将尽快发货')
          cartList.value = []
          updateCart()
          dialogVisible.value = false
          router.push('/')
        } else {
          ElMessage.error(res.msg || '提交失败')
        }
      }).finally(() => {
        submitting.value = false
      })
    }
  })
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.cart-container {
  width: 1200px;
  margin: 20px auto;
  background: #fff;
  padding: 20px;
  min-height: 500px;
}
.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}
.goods-info {
  display: flex;
  align-items: center;
}
.goods-img {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  object-fit: cover;
  border-radius: 4px;
}
.goods-name {
  font-size: 14px;
}
.cart-footer {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #fcfcfc;
  border: 1px solid #eee;
}
.total-text {
  font-size: 16px;
  margin-right: 20px;
}
.total-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
}
</style>