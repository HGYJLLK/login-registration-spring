<template>
  <div class="nav1-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div class="header-right">
            <el-input
              v-model="searchText"
              placeholder="搜索姓名或用户名"
              clearable
              style="width: 250px; margin-right: 10px"
              @input="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleAdd">新增</el-button>
          </div>
        </div>
      </template>

      <!-- 用户列表表格 -->
      <el-table
        :data="filteredUserList"
        border
        style="width: 100%"
        v-loading="loading"
        :row-style="{height: '60px'}"
        :cell-style="{fontSize: '14px'}"
      >
        <el-table-column prop="name" label="姓名" min-width="120" />
        <el-table-column prop="username" label="用户名" min-width="150" />
        <el-table-column prop="password" label="密码" min-width="300" show-overflow-tooltip>
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
      width="700px"
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

    <!-- 新增用户对话框 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增用户"
      width="600px"
      @close="handleAddDialogClose"
    >
      <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="addForm.confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addForm.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="addForm.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddSave">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getAllUsers, updateUser, deleteUser, register } from '../api/user'

const userList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editFormRef = ref(null)
const searchText = ref('')

const editForm = ref({
  id: null,
  name: '',
  username: '',
  password: '',
  gender: '',
  phonenumber: ''
})

// 新增用户相关状态
const addDialogVisible = ref(false)
const addFormRef = ref(null)

const addForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
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

// 新增用户表单验证
const validateAddPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (addForm.value.confirmPassword !== '') {
      addFormRef.value.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== addForm.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const addRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validateAddPassword, trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phonenumber: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 搜索过滤后的用户列表
const filteredUserList = computed(() => {
  if (!searchText.value) {
    return userList.value
  }
  const keyword = searchText.value.toLowerCase()
  return userList.value.filter(user => {
    return (
      (user.name && user.name.toLowerCase().includes(keyword)) ||
      (user.username && user.username.toLowerCase().includes(keyword))
    )
  })
})

// 搜索处理函数
const handleSearch = () => {
  // 搜索由 computed 自动处理
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

// 打开新增用户对话框
const handleAdd = () => {
  addForm.value = {
    username: '',
    password: '',
    confirmPassword: '',
    name: '',
    gender: '',
    phonenumber: ''
  }
  addDialogVisible.value = true
}

// 保存新增用户
const handleAddSave = async () => {
  if (!addFormRef.value) return

  await addFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await register({
          username: addForm.value.username,
          password: addForm.value.password,
          name: addForm.value.name,
          gender: addForm.value.gender,
          phonenumber: addForm.value.phonenumber
        })
        if (res.success) {
          ElMessage.success('新增用户成功')
          addDialogVisible.value = false
          getUserList()
        } else {
          ElMessage.error(res.message || '新增用户失败')
        }
      } catch (error) {
        console.error('新增用户错误:', error)
        ElMessage.error('新增用户失败')
      }
    }
  })
}

// 关闭新增对话框
const handleAddDialogClose = () => {
  if (addFormRef.value) {
    addFormRef.value.resetFields()
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
  background-color: #121212;
  padding: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: #FFFFFF;
}

.header-right {
  display: flex;
  align-items: center;
}

/* Card 深色主题 */
:deep(.el-card) {
  border: none !important; /* 或者 border: 1px solid #333 !important; */
}

:deep(.el-card__header) {
  background-color: #1E1E1E;
  border-bottom: 1px solid #282828;
  color: #FFFFFF;
}

:deep(.el-card__body) {
  height: calc(100% - 60px);
  overflow: auto;
  background-color: #1E1E1E;
}

/* 搜索框深色主题 */
:deep(.el-input__wrapper) {
  background-color: #282828;
  box-shadow: none;
  border: 1px solid #404040;
}

:deep(.el-input__inner) {
  color: #FFFFFF;
}

:deep(.el-input__inner::placeholder) {
  color: #B3B3B3;
}

:deep(.el-icon) {
  color: #B3B3B3;
}

/* 表格深色主题 - 核弹级覆盖 */
:deep(.el-table),
:deep(.el-table th),
:deep(.el-table tr),
:deep(.el-table td) {
  background-color: transparent !important;
  --el-table-bg-color: transparent !important;
  --el-table-tr-bg-color: transparent !important;
  --el-table-header-bg-color: #282828 !important;
  --el-table-row-hover-bg-color: rgba(255, 255, 255, 0.1) !important;
  --el-fill-color-lighter: transparent !important;
  color: #FFFFFF;
}

:deep(.el-table) {
  --el-table-border-color: #333333 !important; /* 关键变量 */
  --el-table-border: 1px solid #333333 !important;
}

:deep(.el-table th) {
  font-size: 15px;
  font-weight: bold;
  background-color: #282828 !important;
  color: #B3B3B3 !important;
  border-color: #404040 !important;
}

:deep(.el-table td) {
  padding: 12px 0;
  background-color: transparent !important;
  color: #FFFFFF !important;
  border-color: #404040 !important;
}

:deep(.el-table--border) {
  border-color: #404040 !important;
}

:deep(.el-table--border::after),
:deep(.el-table--border::before) {
  background-color: #404040 !important;
}

:deep(.el-table__inner-wrapper::before) {
  background-color: #404040 !important;
}

:deep(.el-table .el-table__row:hover) {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

/* 对话框深色主题 */
:deep(.el-dialog) {
  background-color: #1E1E1E;
  border: 1px solid #282828;
}

:deep(.el-dialog__header) {
  background-color: #1E1E1E;
  border-bottom: 1px solid #282828;
}

:deep(.el-dialog__title) {
  color: #FFFFFF;
}

:deep(.el-dialog__body) {
  background-color: #1E1E1E;
  color: #FFFFFF;
}

:deep(.el-dialog__footer) {
  background-color: #1E1E1E;
  border-top: 1px solid #282828;
}

/* 表单深色主题 */
:deep(.el-form-item__label) {
  color: #B3B3B3;
}

/* Select 下拉框深色主题 */
:deep(.el-select-dropdown) {
  background-color: #282828;
  border: 1px solid #404040;
}

:deep(.el-select-dropdown__item) {
  color: #FFFFFF;
}

:deep(.el-select-dropdown__item:hover) {
  background-color: rgba(255, 255, 255, 0.1);
}

:deep(.el-select-dropdown__item.selected) {
  color: #1db954;
}
</style>
