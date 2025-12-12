<template>
  <div class="songs-container">
    <div class="page-header">
      <h2>{{ pageTitle }}</h2>
      <el-input
        v-model="searchText"
        placeholder="搜索歌曲或歌手..."
        style="width: 300px"
        clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <el-table
      :data="filteredSongs"
      style="width: 100%"
      :default-sort="{ prop: 'id', order: 'ascending' }"
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

      <el-table-column label="收藏" width="80" align="center">
        <template #default="{ row }">
          <el-icon
            :class="['favorite-icon', { 'is-favorite': isFavorite(row.id) }]"
            :size="24"
            @click="toggleFavorite(row)"
          >
            <StarFilled v-if="isFavorite(row.id)" />
            <Star v-else />
          </el-icon>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="filteredSongs.length === 0" description="没有找到歌曲" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, inject } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Star, StarFilled, VideoPlay, Pointer } from '@element-plus/icons-vue'
import axios from 'axios'

const route = useRoute()
const songs = ref([])
const singers = ref([])
const searchText = ref('')
const userFavorites = ref([])
const userId = ref(null)
const playSongHandler = inject('playSong')

const pageTitle = computed(() => {
  if (route.query.singerName) {
    return `${route.query.singerName} 的歌曲`
  }
  return '所有歌曲'
})

const filteredSongs = computed(() => {
  if (!searchText.value) {
    return songs.value
  }
  const keyword = searchText.value.toLowerCase()
  return songs.value.filter(song => {
    const singerName = getSingerName(song.singerId).toLowerCase()
    return song.name.toLowerCase().includes(keyword) ||
      singerName.includes(keyword)
  })
})

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

const isFavorite = (songId) => {
  return userFavorites.value.some(fav => fav.id === songId)
}

const toggleFavorite = async (song) => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isFavorite(song.id)) {
      await axios.delete('http://localhost:8082/api/music/favorite/remove', {
        params: { userId: storedUserId, songId: song.id }
      })
      ElMessage.success('已取消收藏')
      await loadUserFavorites()
    } else {
      await axios.post('http://localhost:8082/api/music/favorite/add', {
        userId: storedUserId,
        songId: song.id
      })
      ElMessage.success('收藏成功')
      await loadUserFavorites()
    }
  } catch (error) {
    console.error('收藏操作失败', error)
    ElMessage.error('操作失败')
  }
}

const handleSearch = () => {
  // Search is handled by computed property
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

const loadSongs = async () => {
  try {
    let res
    if (route.query.singerId) {
      res = await axios.get('http://localhost:8082/api/music/song/singer/detail', {
        params: { singerId: route.query.singerId }
      })
    } else {
      res = await axios.get('http://localhost:8082/api/music/song')
    }
    songs.value = res.data
  } catch (error) {
    console.error('加载歌曲失败', error)
    ElMessage.error('加载歌曲失败')
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

const loadUserFavorites = async () => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) return
  try {
    const res = await axios.get('http://localhost:8082/api/music/favorite/list', {
      params: { userId: storedUserId }
    })
    userFavorites.value = res.data
  } catch (error) {
    console.error('加载收藏失败', error)
  }
}

onMounted(() => {
  userId.value = localStorage.getItem('userId')
  loadSongs()
  loadSingers()
  loadUserFavorites()
})
</script>

<style scoped>
.songs-container {
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

.favorite-icon {
  cursor: pointer;
  color: #B3B3B3;
  transition: all 0.3s;
}

.favorite-icon:hover {
  color: #1db954;
  transform: scale(1.2);
}

.favorite-icon.is-favorite {
  color: #e74c3c;
}

.favorite-icon.is-favorite:hover {
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

:deep(.el-table__body tr.current-row > td) {
  background-color: #282828;
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

:deep(.el-empty) {
  background-color: transparent;
}

:deep(.el-empty__description p) {
  color: #B3B3B3;
}
</style>
