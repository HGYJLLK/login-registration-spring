<template>
  <div class="favorites-container">
    <div class="page-header">
      <h2>个人收藏</h2>
      <div class="stats">
        <span>共 {{ favorites.length }} 首歌曲</span>
      </div>
    </div>

    <el-table
      :data="favorites"
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column label="封面" width="80">
        <template #default="{ row }">
          <img
            :src="getSongPic(row.picUrl)"
            class="song-pic"
            @error="handleImageError"
          />
        </template>
      </el-table-column>

      <el-table-column label="歌曲名" min-width="250">
        <template #default="{ row }">
          <div class="song-info">
            <div class="song-name">{{ row.name }}</div>
            <div class="singer-name-sub">{{ getSingerName(row.singerId) }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="播放/点赞" width="130" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-icon
              class="play-icon"
              :size="26"
              @click="playSong(row)"
              title="播放"
            >
              <VideoPlay />
            </el-icon>
            <el-icon
              class="like-icon"
              :size="24"
              @click="likeSong(row)"
              title="点赞"
            >
              <Pointer />
            </el-icon>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="收藏" width="120" align="center">
        <template #default="{ row }">
          <el-button
            type="danger"
            size="small"
            @click="removeFavorite(row)"
          >
            取消收藏
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="favorites.length === 0 && !loading" description="还没有收藏任何歌曲">
      <el-button type="primary" @click="goToSongs">去发现音乐</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { VideoPlay, Pointer } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const favorites = ref([])
const singers = ref([])
const loading = ref(false)
const userId = ref(null)
const playSongHandler = inject('playSong')

const getSongPic = (pic) => {
  if (!pic) return '/music.svg'
  if (pic.startsWith('http')) return pic
  return `http://localhost:8082${pic}`
}

const handleImageError = (e) => {
  e.target.src = '/music.svg'
}

const getSingerName = (singerId) => {
  const singer = singers.value.find(s => s.id === singerId)
  return singer ? singer.name : '未知歌手'
}

const removeFavorite = async (song) => {
  ElMessageBox.confirm(`确定要取消收藏 "${song.name}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const storedUserId = localStorage.getItem('userId')
    try {
      await axios.delete('http://localhost:8082/api/music/favorite/remove', {
        params: { userId: storedUserId, songId: song.id }
      })
      ElMessage.success('已取消收藏')
      await loadFavorites()
    } catch (error) {
      console.error('取消收藏失败', error)
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

const goToSongs = () => {
  router.push('/home/songs')
}

const playSong = (song) => {
  if (playSongHandler) {
    playSongHandler(song)
    ElMessage.success(`正在播放: ${song.name}`)
  }
}

const likeSong = (song) => {
  ElMessage.success(`已点赞: ${song.name}`)
}

const loadFavorites = async () => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  loading.value = true
  try {
    const res = await axios.get('http://localhost:8082/api/music/favorite/list', {
      params: { userId: storedUserId }
    })
    favorites.value = res.data
  } catch (error) {
    console.error('加载收藏失败', error)
    ElMessage.error('加载收藏失败')
  } finally {
    loading.value = false
  }
}

const loadSingers = async () => {
  try {
    const res = await axios.get('http://localhost:8082/api/music/singer')
    singers.value = res.data
  } catch (error) {
    console.error('加载歌手失败', error)
  }
}

onMounted(() => {
  userId.value = localStorage.getItem('userId')
  loadFavorites()
  loadSingers()
})
</script>

<style scoped>
.favorites-container {
  padding: 24px;
  background-color: #121212;
  min-height: 100%;
  color: #FFFFFF;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #FFFFFF;
}

.stats {
  color: #B3B3B3;
  font-size: 14px;
}

.song-pic {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}

.song-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.song-name {
  color: #FFFFFF;
  font-weight: 500;
  font-size: 15px;
}

.singer-name-sub {
  color: #B3B3B3;
  font-size: 13px;
}

.action-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  align-items: center;
}

.play-icon, .like-icon {
  cursor: pointer;
  color: #B3B3B3;
  transition: all 0.3s;
}

.play-icon:hover {
  color: #1db954;
  transform: scale(1.2);
}

.like-icon:hover {
  color: #1db954;
  transform: scale(1.2);
}

:deep(.el-table) {
  background-color: transparent;
  color: #FFFFFF;
}

:deep(.el-table th.el-table__cell) {
  background-color: #181818;
  color: #FFFFFF;
  border-bottom: 1px solid #282828;
}

:deep(.el-table tr) {
  background-color: transparent;
}

:deep(.el-table td.el-table__cell) {
  border-bottom: 1px solid #282828;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #282828 !important;
}

:deep(.el-empty) {
  background-color: transparent;
}

:deep(.el-empty__description p) {
  color: #B3B3B3;
}

:deep(.el-loading-mask) {
  background-color: rgba(18, 18, 18, 0.8);
}
</style>
