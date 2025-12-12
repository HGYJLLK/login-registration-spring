<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <img src="/music.svg" alt="音乐" class="header-icon" />
          <span>音乐播放器注册</span>
        </div>
      </template>

      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
          />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-select v-model="registerForm.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>

        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="registerForm.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>

        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-avatar
              :src="getAvatarUrl(registerForm.avatarUrl)"
              :size="80"
              class="avatar-preview"
            />
            <el-upload
              :action="uploadUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              accept="image/*"
            >
              <el-button type="primary" size="small">
                选择头像
              </el-button>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">
            注册
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button @click="goToLogin" style="width: 100%">
            返回登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/user'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  gender: '',
  phonenumber: '',
  avatarUrl: ''
})

const uploadUrl = 'http://localhost:8082/api/file/avatar'

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      registerFormRef.value.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePassword, trigger: 'blur' },
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

const getAvatarUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8082${url}`
}

const handleAvatarSuccess = (response) => {
  if (response.url) {
    registerForm.avatarUrl = response.url
    ElMessage.success('头像上传成功')
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

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await register({
          username: registerForm.username,
          password: registerForm.password,
          name: registerForm.name,
          gender: registerForm.gender,
          phonenumber: registerForm.phonenumber,
          avatarUrl: registerForm.avatarUrl
        })
        if (res.success) {
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        } else {
          ElMessage.error(res.message || '注册失败')
        }
      } catch (error) {
        console.error('注册错误:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px 0;
  background-color: #121212;
}

.register-card {
  width: 500px;
  background-color: #181818;
  border: 1px solid #282828;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-size: 24px;
  font-weight: bold;
  color: #FFFFFF;
}

.header-icon {
  width: 32px;
  height: 32px;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-preview {
  background-color: #282828;
  border: 2px solid #1db954;
}

/* Card 深色主题 */
:deep(.el-card) {
  background-color: #181818;
  border: 1px solid #282828;
}

:deep(.el-card__header) {
  background-color: #1E1E1E;
  border-bottom: 1px solid #282828;
  color: #FFFFFF;
}

:deep(.el-card__body) {
  background-color: #181818;
}

/* 表单深色主题 */
:deep(.el-form-item__label) {
  color: #B3B3B3;
}

:deep(.el-input__wrapper) {
  background-color: #282828;
  box-shadow: none;
  border: 1px solid #404040;
}

:deep(.el-input__inner) {
  color: #FFFFFF;
}

:deep(.el-input__inner::placeholder) {
  color: #666666;
}

:deep(.el-input__wrapper:hover) {
  border-color: #1db954;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #1db954;
  box-shadow: 0 0 0 1px #1db954 inset;
}

/* Select 下拉框深色主题 */
:deep(.el-select__wrapper) {
  background-color: #282828;
  box-shadow: none;
  border: 1px solid #404040;
}

:deep(.el-select__wrapper:hover) {
  border-color: #1db954;
}

:deep(.el-select__wrapper.is-focused) {
  border-color: #1db954;
  box-shadow: 0 0 0 1px #1db954 inset;
}

:deep(.el-select__placeholder) {
  color: #666666;
}

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
  background-color: rgba(29, 185, 84, 0.1);
}

/* 按钮样式 */
:deep(.el-button--primary) {
  background-color: #1db954;
  border-color: #1db954;
}

:deep(.el-button--primary:hover) {
  background-color: #1ed760;
  border-color: #1ed760;
}

:deep(.el-button--default) {
  background-color: transparent;
  border-color: #B3B3B3;
  color: #FFFFFF;
}

:deep(.el-button--default:hover) {
  border-color: #FFFFFF;
  color: #FFFFFF;
  background-color: rgba(255, 255, 255, 0.1);
}
</style>
