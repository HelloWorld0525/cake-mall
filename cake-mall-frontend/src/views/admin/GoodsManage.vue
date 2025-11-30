<template>
  <div>
    <!-- 搜索与操作 -->
    <div style="margin-bottom: 20px;">
      <el-input 
        v-model="searchName" 
        placeholder="请输入商品名称" 
        style="width: 200px; margin-right: 10px;" 
        clearable 
        @clear="loadData"
      />
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="success" @click="handleAdd">新增商品</el-button>
    </div>

    <!-- 表格展示 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column label="封面图" width="100" align="center">
        <template #default="scope">
          <el-image 
            style="width: 50px; height: 50px" 
            :src="scope.row.cover" 
            :preview-src-list="[scope.row.cover]" 
            preview-teleported
            fit="cover" 
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" show-overflow-tooltip />
      <el-table-column prop="price" label="价格(元)" width="100">
        <template #default="scope">
           ￥{{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column label="所属类目" width="120">
        <template #default="scope">
          <el-tag>{{ getTypeName(scope.row.typeId) }}</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
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

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑商品' : '新增商品'" width="50%" top="5vh">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        
        <el-form-item label="商品类目" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择类目" style="width: 100%;">
            <el-option 
              v-for="item in typeList" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id" 
            />
          </el-select>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存" prop="stock">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="封面图" prop="cover">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
          >
            <img v-if="form.cover" :src="form.cover" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="商品介绍">
          <el-input type="textarea" v-model="form.intro" rows="3" />
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
import { Plus } from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const typeList = ref([]) // 类目列表
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchName = ref('')
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  price: 0,
  stock: 0,
  typeId: null,
  cover: '',
  intro: ''
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  typeId: [{ required: true, message: '请选择类目', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  cover: [{ required: true, message: '请上传封面图', trigger: 'change' }]
}

// 加载类目数据 (用于下拉框和表格显示)
const loadTypes = () => {
  request.get('/type/findAll').then(res => {
    if (res.code === '200') {
      typeList.value = res.data
    }
  })
}

// 辅助函数：根据ID获取类目名称
const getTypeName = (id) => {
  const type = typeList.value.find(t => t.id === id)
  return type ? type.name : '未知类目'
}

// 加载商品列表
const loadData = () => {
  loading.value = true
  request.get('/goods/list', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: searchName.value
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

// 封面上传成功回调
const handleCoverSuccess = (res) => {
  if (res.code === '200') {
    form.cover = res.data // 回填 URL
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

// 新增
const handleAdd = () => {
  form.id = null
  form.name = ''
  form.price = 0
  form.stock = 0
  form.typeId = null
  form.cover = ''
  form.intro = ''
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

// 保存
const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      const method = form.id ? 'put' : 'post'
      request[method]('/goods', form).then(res => {
        if (res.code === '200') {
          ElMessage.success('操作成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

// 删除
const handleDelete = (id) => {
  request.delete('/goods/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  loadTypes()
  loadData()
})
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px;
  border: 1px dashed #ccc;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>