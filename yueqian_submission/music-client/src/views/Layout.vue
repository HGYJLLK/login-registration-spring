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
            <el-avatar
              v-if="userInfo.avatarUrl"
              :src="getAvatarUrl(userInfo.avatarUrl)"
              :size="32"
              style="margin-right: 8px;"
            />
            <el-icon v-else><User /></el-icon>
            {{ username }}
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
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
          <el-menu-item index="/home/singers">
            <el-icon><Microphone /></el-icon>
            <span>歌手</span>
          </el-menu-item>
          <el-menu-item index="/home/songlists">
            <el-icon><Menu /></el-icon>
            <span>歌单</span>
          </el-menu-item>
          <el-menu-item index="/home/songs">
            <el-icon><Headset /></el-icon>
            <span>所有歌曲</span>
          </el-menu-item>
          <el-menu-item index="/home/favorites">
            <el-icon><Star /></el-icon>
            <span>个人收藏</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容 -->
      <el-main class="main">
        <router-view @play-song="handlePlaySong" :singers="singers" />
      </el-main>
    </el-container>

    <!-- 音乐播放器 -->
    <MusicPlayer
      :song="currentSong"
      :singers="singers"
      @play-next="handlePlayNext"
      @play-previous="handlePlayPrevious"
    />
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, provide } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, ArrowDown, Headset, Microphone, Menu, Star } from '@element-plus/icons-vue'
import axios from 'axios'
import MusicPlayer from '../components/MusicPlayer.vue'

const router = useRouter()
const route = useRoute()

const username = ref('')
const userInfo = ref({})
const activeMenu = computed(() => route.path)
const currentSong = ref(null)
const singers = ref([])
const playlist = ref([])
const currentIndex = ref(-1)

const handlePlaySong = (song) => {
  currentSong.value = song
  // 更新播放列表中的当前索引
  const index = playlist.value.findIndex(s => s.id === song.id)
  if (index !== -1) {
    currentIndex.value = index
  } else {
    // 如果歌曲不在播放列表中，添加到播放列表
    playlist.value.push(song)
    currentIndex.value = playlist.value.length - 1
  }
}

const handlePlayNext = () => {
  if (playlist.value.length === 0) return null
  currentIndex.value = (currentIndex.value + 1) % playlist.value.length
  currentSong.value = playlist.value[currentIndex.value]
  return currentSong.value
}

const handlePlayPrevious = () => {
  if (playlist.value.length === 0) return null
  currentIndex.value = currentIndex.value - 1
  if (currentIndex.value < 0) {
    currentIndex.value = playlist.value.length - 1
  }
  currentSong.value = playlist.value[currentIndex.value]
  return currentSong.value
}

const setPlaylist = (songs) => {
  playlist.value = songs
}

provide('playSong', handlePlaySong)
provide('setPlaylist', setPlaylist)

const getAvatarUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8082${url}`
}

const loadUserInfo = async () => {
  username.value = localStorage.getItem('username') || '用户'
  const userId = localStorage.getItem('userId')

  if (userId) {
    try {
      const res = await axios.get(`http://localhost:8082/api/users`)
      const user = res.data.find(u => u.id == userId)
      if (user) {
        userInfo.value = user
        username.value = user.name || user.username
      }
    } catch (error) {
      console.error('加载用户信息失败', error)
    }
  }
}

const handleAvatarUpdate = (event) => {
  if (event.detail && event.detail.avatarUrl) {
    userInfo.value.avatarUrl = event.detail.avatarUrl
  }
}

onMounted(async () => {
  await loadUserInfo()

  // 加载歌手列表
  try {
    const res = await axios.get('http://localhost:8082/api/music/singer')
    singers.value = res.data
  } catch (error) {
    console.error('加载歌手失败', error)
  }

  // 监听头像更新事件
  window.addEventListener('user-avatar-updated', handleAvatarUpdate)
})

onBeforeUnmount(() => {
  // 清理事件监听器
  window.removeEventListener('user-avatar-updated', handleAvatarUpdate)
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
      localStorage.removeItem('userId')
      localStorage.removeItem('token')
      ElMessage.success('已退出登录')
      router.push('/login')
    }).catch(() => {})
  } else if (command === 'profile') {
    router.push('/home/profile')
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
  gap: 16px;
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
  padding-bottom: 90px;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}
</style>
