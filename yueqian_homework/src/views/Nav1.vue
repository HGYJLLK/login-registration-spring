<template>
  <div class="nav1-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
        </div>
      </template>

      <!-- 用户列表表格 -->
      <el-table
        :data="userList"
        border
        style="width: 100%"
        v-loading="loading"
        :row-style="{height: '60px'}"
        :cell-style="{fontSize: '14px'}"
      >
        <el-table-column prop="name" label="姓名" min-width="120" />
        <el-table-column prop="username" label="用户名" min-width="150" />
        <el-table-column prop="password" label="密码" min-width="400" show-overflow-tooltip>
          <template #default="scope">
            <span style="font-family: monospace; font-size: 12px;">{{ scope.row.password }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="100" />
        <el-table-column prop="phonenumber" label="手机号码" min-width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="default" @click="handleEdit(scope.row)">
              修改
            </el-button>
            <el-button type="danger" size="default" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改用户信息"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="editForm.password" type="password" placeholder="不修改请留空" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editForm.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="editForm.phonenumber" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllUsers, updateUser, deleteUser } from '../api/user'

const userList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editFormRef = ref(null)

const editForm = ref({
  id: null,
  name: '',
  username: '',
  password: '',
  gender: '',
  phonenumber: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phonenumber: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 获取用户列表
const getUserList = async () => {
  loading.value = true
  try {
    const res = await getAllUsers()
    userList.value = res
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 编辑用户
const handleEdit = (row) => {
  editForm.value = {
    id: row.id,
    name: row.name,
    username: row.username,
    password: '',
    gender: row.gender,
    phonenumber: row.phonenumber
  }
  dialogVisible.value = true
}

// 保存修改
const handleSave = async () => {
  if (!editFormRef.value) return

  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const updateData = {
          name: editForm.value.name,
          username: editForm.value.username,
          gender: editForm.value.gender,
          phonenumber: editForm.value.phonenumber
        }

        // 如果密码不为空，则包含密码字段
        if (editForm.value.password) {
          updateData.password = editForm.value.password
        }

        await updateUser(editForm.value.id, updateData)
        ElMessage.success('修改成功')
        dialogVisible.value = false
        getUserList()
      } catch (error) {
        console.error('修改失败:', error)
      }
    }
  })
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      getUserList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

// 关闭对话框
const handleDialogClose = () => {
  if (editFormRef.value) {
    editFormRef.value.resetFields()
  }
}

onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.nav1-container {
  width: 100%;
  height: 100%;
}

.card-header {
  font-size: 20px;
  font-weight: bold;
}

:deep(.el-card) {
  height: 100%;
}

:deep(.el-card__body) {
  height: calc(100% - 60px);
  overflow: auto;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  font-size: 15px;
  font-weight: bold;
  background-color: #f5f7fa;
}

:deep(.el-table td) {
  padding: 12px 0;
}
</style>
