<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <img src="/music.svg" alt="音乐" class="header-icon" />
          <span>音乐播放器登录</span>
        </div>
      </template>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button @click="goToRegister" style="width: 100%">
            注册
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
import { login } from '../api/user'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 判断是否为管理员
        if (loginForm.username === 'admin' && loginForm.password === 'admin') {
          // 管理员直接登录，不调用后端接口
          localStorage.setItem('username', 'admin')
          localStorage.setItem('userId', '0')
          localStorage.setItem('isAdmin', 'true')
          ElMessage.success('管理员登录成功')
          router.push('/admin')
        } else {
          // 普通用户调用后端接口验证
          const res = await login(loginForm)
          if (res.success) {
            // 保存用户信息
            localStorage.setItem('username', res.username || loginForm.username)
            localStorage.setItem('userId', res.userId)
            localStorage.removeItem('isAdmin')
            if (res.name) {
              localStorage.setItem('name', res.name)
            }
            ElMessage.success('登录成功')
            router.push('/home')
          } else {
            ElMessage.error(res.message || '登录失败')
          }
        }
      } catch (error) {
        console.error('登录错误:', error)
        ElMessage.error('登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #121212;
}

.login-card {
  width: 400px;
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
