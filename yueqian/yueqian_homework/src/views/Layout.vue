<template>
  <el-container class="layout-container">
    <!-- 顶部栏 -->
    <el-header class="header">
      <div class="header-left">
        <img src="/music.svg" alt="音乐" class="header-icon" />
        <span>音乐播放器</span>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <el-icon><User /></el-icon>
            {{ username }}
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 内容区域 -->
    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="aside">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          @select="handleMenuSelect"
          background-color="#000000"
          text-color="#B3B3B3"
          active-text-color="#1db954"
        >
          <el-menu-item index="/home/nav1">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/home/music">
            <el-icon><Headset /></el-icon>
            <span>音乐播放器</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, ArrowDown, Headset } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const username = ref('')
const activeMenu = computed(() => route.path)

onMounted(() => {
  username.value = localStorage.getItem('username') || '用户'
})

const handleMenuSelect = (index) => {
  router.push(index)
}

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('username')
      localStorage.removeItem('token')
      ElMessage.success('已退出登录')
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #121212;
  border-bottom: 1px solid #282828;
  padding: 0 20px;
  height: 60px;
  z-index: 1000;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
}

.header-icon {
  width: 28px;
  height: 28px;
}

.header-right {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #FFFFFF;
  gap: 8px;
}

.el-dropdown-link:hover {
  color: #1db954;
}

.main-container {
  flex: 1;
  overflow: hidden;
}

.aside {
  background-color: #000000;
  color: #fff;
  height: 100%;
}

.el-menu-vertical {
  border-right: none;
  height: 100%;
}

.main {
  background-color: #121212;
  padding: 0;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}
</style>
