<template>
  <div>
    <!-- 操作栏 -->
    <div style="margin-bottom: 20px;">
      <el-button type="primary" @click="handleAdd">新增类目</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="100" align="center" />
      <el-table-column prop="name" label="类目名称" />
      
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除该类目吗？" @confirm="handleDelete(scope.row.id)">
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

    <!-- 新增/编辑 对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑类目' : '新增类目'" width="30%">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入类目名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: ''
})

const rules = {
  name: [{ required: true, message: '请输入类目名称', trigger: 'blur' }]
}

// 加载列表
const loadData = () => {
  loading.value = true
  request.get('/type/list', {
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

// 打开新增框
const handleAdd = () => {
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

// 打开编辑框
const handleEdit = (row) => {
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

// 提交保存
const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (form.id) {
        // 更新
        request.put('/type', form).then(res => {
          if (res.code === '200') {
            ElMessage.success('更新成功')
            dialogVisible.value = false
            loadData()
          } else {
            ElMessage.error(res.msg)
          }
        })
      } else {
        // 新增
        request.post('/type', form).then(res => {
          if (res.code === '200') {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            loadData()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    }
  })
}

// 删除
const handleDelete = (id) => {
  request.delete('/type/' + id).then(res => {
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