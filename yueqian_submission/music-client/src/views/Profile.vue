<template>
  <div class="profile-container">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <el-card class="profile-card">
      <div class="profile-content">
        <div class="avatar-section">
          <el-avatar
            :src="getAvatarUrl(userInfo.avatarUrl)"
            :size="120"
            class="profile-avatar"
          />
          <el-upload
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            accept="image/*"
          >
            <el-button type="primary" size="small" style="margin-top: 12px;">
              更换头像
            </el-button>
          </el-upload>
        </div>

        <el-form
          :model="userForm"
          :rules="rules"
          ref="formRef"
          label-width="100px"
          class="profile-form"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userForm.username" disabled />
          </el-form-item>

          <el-form-item label="昵称" prop="name">
            <el-input
              v-model="userForm.name"
              placeholder="请输入昵称"
              clearable
            />
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="userForm.email"
              placeholder="请输入邮箱"
              clearable
            />
          </el-form-item>

          <el-divider />

          <h3 style="margin-bottom: 20px;">修改密码</h3>

          <el-form-item label="旧密码" prop="oldPassword">
            <el-input
              v-model="userForm.oldPassword"
              type="password"
              placeholder="如需修改密码请输入旧密码"
              clearable
              show-password
            />
          </el-form-item>

          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="userForm.newPassword"
              type="password"
              placeholder="请输入新密码"
              clearable
              show-password
            />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input
              v-model="userForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              clearable
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="saveProfile" :loading="saving">
              保存修改
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const formRef = ref(null)
const userInfo = ref({})
const userForm = reactive({
  id: null,
  username: '',
  name: '',
  email: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const saving = ref(false)
const uploadUrl = 'http://localhost:8082/api/file/avatar'

const validatePassword = (rule, value, callback) => {
  if (value && value.length < 6) {
    callback(new Error('密码长度不能小于6位'))
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value && value !== userForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  name: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  newPassword: [
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const getAvatarUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8082${url}`
}

const handleAvatarSuccess = async (response) => {
  if (response.url) {
    userInfo.value.avatarUrl = response.url
    ElMessage.success('头像上传成功')
    // 自动保存头像
    try {
      await axios.put(`http://localhost:8082/api/users/${userForm.id}`, {
        avatarUrl: response.url
      })
      ElMessage.success('头像已保存')
      // 触发存储事件通知其他组件更新
      window.dispatchEvent(new CustomEvent('user-avatar-updated', { detail: { avatarUrl: response.url } }))
    } catch (error) {
      console.error('保存头像失败', error)
      ElMessage.error('保存头像失败')
    }
  } else {
    ElMessage.error('头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const saveProfile = async () => {
  if (!formRef.value) return

  formRef.value.validate(async (valid) => {
    if (!valid) return

    // 如果填写了密码，检查旧密码是否正确
    if (userForm.newPassword) {
      if (!userForm.oldPassword) {
        ElMessage.warning('请输入旧密码')
        return
      }
      if (userForm.oldPassword !== userInfo.value.password) {
        ElMessage.error('旧密码不正确')
        return
      }
    }

    saving.value = true
    try {
      const updateData = {
        name: userForm.name,
        email: userForm.email,
        avatarUrl: userInfo.value.avatarUrl
      }

      // 如果修改了密码
      if (userForm.newPassword) {
        updateData.password = userForm.newPassword
      }

      await axios.put(`http://localhost:8082/api/users/${userForm.id}`, updateData)

      ElMessage.success('保存成功')

      // 更新本地存储的用户名
      if (userForm.name !== localStorage.getItem('username')) {
        localStorage.setItem('username', userForm.name)
      }

      // 清空密码字段
      userForm.oldPassword = ''
      userForm.newPassword = ''
      userForm.confirmPassword = ''

      // 重新加载用户信息
      await loadUserInfo()
    } catch (error) {
      console.error('保存失败', error)
      ElMessage.error('保存失败')
    } finally {
      saving.value = false
    }
  })
}

const resetForm = () => {
  userForm.name = userInfo.value.name || userInfo.value.username
  userForm.email = userInfo.value.email || ''
  userForm.oldPassword = ''
  userForm.newPassword = ''
  userForm.confirmPassword = ''
  formRef.value?.clearValidate()
}

const loadUserInfo = async () => {
  const userId = localStorage.getItem('userId')
  if (!userId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    const res = await axios.get('http://localhost:8082/api/users')
    const user = res.data.find(u => u.id == userId)
    if (user) {
      userInfo.value = user
      userForm.id = user.id
      userForm.username = user.username
      userForm.name = user.name || user.username
      userForm.email = user.email || ''
    }
  } catch (error) {
    console.error('加载用户信息失败', error)
    ElMessage.error('加载用户信息失败')
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 24px;
  background-color: #121212;
  min-height: 100%;
  color: #FFFFFF;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #FFFFFF;
}

.profile-card {
  max-width: 800px;
  background-color: #181818;
  border: none;
}

.profile-content {
  display: flex;
  gap: 48px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;
}

.profile-avatar {
  background-color: #282828;
  border: 3px solid #1db954;
}

.profile-form {
  flex: 1;
}

:deep(.el-card__body) {
  background-color: #181818;
}

:deep(.el-form-item__label) {
  color: #FFFFFF;
}

:deep(.el-input__wrapper) {
  background-color: #282828;
  box-shadow: none;
}

:deep(.el-input__inner) {
  color: #FFFFFF;
}

:deep(.el-input__inner)::placeholder {
  color: #B3B3B3;
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background-color: #1a1a1a;
}

:deep(.el-input.is-disabled .el-input__inner) {
  color: #666666;
}

:deep(.el-divider) {
  border-color: #282828;
}

h3 {
  color: #FFFFFF;
  font-size: 18px;
}

:deep(.el-upload) {
  display: block;
}
</style>
