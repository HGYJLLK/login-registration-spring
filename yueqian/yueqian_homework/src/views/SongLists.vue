<template>
  <div class="songlists-container">
    <div class="page-header">
      <h2>歌单</h2>
      <el-input
        v-model="searchText"
        placeholder="搜索歌单..."
        style="width: 300px"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <div class="songlists-grid">
      <div
        v-for="songList in filteredSongLists"
        :key="songList.id"
        class="songlist-card"
        @click="viewSongListDetail(songList)"
      >
        <div class="songlist-pic">
          <img
            :src="getSongListPic(songList.picUrl)"
            :alt="songList.title"
            @error="handleImageError"
          />
        </div>
        <div class="songlist-info">
          <div class="songlist-title">{{ songList.title }}</div>
          <div class="songlist-intro">{{ songList.introduction || '暂无简介' }}</div>
        </div>
      </div>
    </div>

    <el-empty v-if="filteredSongLists.length === 0" description="没有找到歌单" />

    <!-- 歌单详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="currentSongList.title"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="songlist-detail">
        <div class="detail-header">
          <img
            :src="getSongListPic(currentSongList.picUrl)"
            class="detail-pic"
            @error="handleImageError"
          />
          <div class="detail-info">
            <h3>{{ currentSongList.title }}</h3>
            <el-button type="primary" round @click="playAll" v-if="songListSongs.length > 0" style="margin-bottom: 12px;">
              <el-icon style="margin-right: 4px"><VideoPlay /></el-icon> 播放全部
            </el-button>
            <p class="style-tag">风格: {{ currentSongList.style || '未分类' }}</p>
          </div>
        </div>

        <el-divider />

        <div class="songs-list">
          <h4>歌曲列表</h4>
          <el-table :data="songListSongs" style="width: 100%">
            <el-table-column type="index" label="#" width="50" />
            <el-table-column prop="name" label="歌曲名" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  size="small"
                  @click.stop="handlePlaySong(row)"
                  style="margin-right: 8px;"
                >
                  <el-icon style="margin-right: 4px"><VideoPlay /></el-icon>
                  播放
                </el-button>
                <el-button
                  :icon="isFavorite(row.id) ? StarFilled : Star"
                  :type="isFavorite(row.id) ? 'warning' : 'default'"
                  size="small"
                  @click.stop="toggleFavorite(row)"
                >
                  {{ isFavorite(row.id) ? '已收藏' : '收藏' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, inject } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Star, StarFilled, VideoPlay } from '@element-plus/icons-vue'
import axios from 'axios'

const songLists = ref([])
const searchText = ref('')
const dialogVisible = ref(false)
const currentSongList = ref({})
const songListSongs = ref([])
const userFavorites = ref([])
const userId = ref(null)

const playSong = inject('playSong')
const setPlaylist = inject('setPlaylist')

const playAll = () => {
  if (songListSongs.value && songListSongs.value.length > 0) {
    // 设置播放列表
    if (setPlaylist) {
      setPlaylist(songListSongs.value)
    }
    // 播放第一首
    if (playSong) {
      playSong(songListSongs.value[0])
      ElMessage.success('开始播放歌单')
    }
  } else {
    ElMessage.warning('歌单为空')
  }
}

const handlePlaySong = (song) => {
  // 设置播放列表
  if (setPlaylist) {
    setPlaylist(songListSongs.value)
  }
  // 播放选中的歌曲
  if (playSong) {
    playSong(song)
    ElMessage.success(`正在播放: ${song.name}`)
  }
}

const filteredSongLists = computed(() => {
  if (!searchText.value) {
    return songLists.value
  }
  const keyword = searchText.value.toLowerCase()
  return songLists.value.filter(list =>
    list.title.toLowerCase().includes(keyword) ||
    (list.introduction && list.introduction.toLowerCase().includes(keyword))
  )
})

const getSongListPic = (pic) => {
  if (!pic) return '/music.svg'
  if (pic.startsWith('http')) return pic
  return `http://localhost:8082${pic}`
}

const handleImageError = (e) => {
  e.target.src = '/music.svg'
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

const viewSongListDetail = async (songList) => {
  currentSongList.value = songList
  dialogVisible.value = true
  try {
    const res = await axios.get('http://localhost:8082/api/music/songlist/songs', {
      params: { id: songList.id }
    })
    songListSongs.value = res.data
  } catch (error) {
    console.error('加载歌单歌曲失败', error)
    ElMessage.error('加载歌单歌曲失败')
  }
}

const loadSongLists = async () => {
  try {
    const res = await axios.get('http://localhost:8082/api/music/songlist/all')
    songLists.value = res.data
  } catch (error) {
    console.error('加载歌单失败', error)
    ElMessage.error('加载歌单失败')
  }
}

const loadUserFavorites = async () => {
  if (!userId.value) return
  try {
    const res = await axios.get('http://localhost:8082/api/music/favorite/list', {
      params: { userId: userId.value }
    })
    userFavorites.value = res.data
  } catch (error) {
    console.error('加载收藏失败', error)
  }
}

onMounted(() => {
  userId.value = localStorage.getItem('userId')
  loadSongLists()
  loadUserFavorites()
})
</script>

<style scoped>
.songlists-container {
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

.songlists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
}

.songlist-card {
  background-color: #181818;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.songlist-card:hover {
  background-color: #282828;
}

.songlist-pic {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 12px;
  background-color: #282828;
}

.songlist-pic img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.songlist-info {
  text-align: left;
}

.songlist-title {
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.songlist-intro {
  font-size: 14px;
  color: #B3B3B3;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.songlist-detail {
  color: #FFFFFF;
}

.detail-header {
  display: flex;
  gap: 24px;
}

.detail-pic {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
}

.detail-info {
  flex: 1;
}

.detail-info h3 {
  margin: 0 0 16px 0;
  font-size: 24px;
}

.detail-info p {
  color: #B3B3B3;
  line-height: 1.6;
}

.style-tag {
  margin-top: 12px;
  font-weight: 500;
  color: #1db954 !important;
}

.songs-list h4 {
  margin-bottom: 16px;
  color: #FFFFFF;
}

:deep(.el-dialog) {
  background-color: #181818;
}

:deep(.el-dialog__title) {
  color: #FFFFFF;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #FFFFFF;
}

:deep(.el-table) {
  background-color: transparent;
  color: #FFFFFF;
}

:deep(.el-table th.el-table__cell) {
  background-color: #282828;
  color: #FFFFFF;
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

:deep(.el-divider) {
  border-color: #282828;
}

:deep(.el-empty) {
  background-color: transparent;
}

:deep(.el-empty__description p) {
  color: #B3B3B3;
}
</style>
