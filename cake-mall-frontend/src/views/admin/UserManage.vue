<template>
  <div>
    <!-- 搜索栏 -->
    <div style="margin-bottom: 20px;">
      <el-input 
        v-model="searchUsername" 
        placeholder="请输入用户名搜索" 
        style="width: 200px; margin-right: 10px;" 
        clearable
        @clear="loadData"
      />
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="address" label="地址" show-overflow-tooltip />
      
      <el-table-column prop="isadmin" label="角色" width="100" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.isadmin" type="danger">管理员</el-tag>
          <el-tag v-else type="success">普通用户</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-popconfirm title="确定要删除该用户吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
          <!-- 预留编辑功能 -->
           <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
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
    
    <!-- 编辑对话框 (简单实现) -->
    <el-dialog v-model="dialogVisible" title="修改用户信息" width="30%">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="editForm.address" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchUsername = ref('')

// 编辑相关
const dialogVisible = ref(false)
const editForm = reactive({})

// 加载数据
const loadData = () => {
  loading.value = true
  request.get('/user/list', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      username: searchUsername.value
    }
  }).then(res => {
    if (res.code === '200') {
      tableData.value = res.data.records
      total.value = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  }).finally(() => {
    loading.value = false
  })
}

// 删除用户
const handleDelete = (id) => {
  request.delete('/user/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 打开编辑框
const handleEdit = (row) => {
  // 浅拷贝，防止直接修改表格显示
  Object.assign(editForm, row)
  dialogVisible.value = true
}

// 提交修改
const submitEdit = () => {
  request.put('/user', editForm).then(res => {
    if (res.code === '200') {
      ElMessage.success('修改成功')
      dialogVisible.value = false
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