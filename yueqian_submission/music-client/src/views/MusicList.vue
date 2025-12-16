<template>
  <div class="music-list">
    <!-- 顶部搜索栏 -->
    <div class="header">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索歌曲、歌手..."
        class="search-bar"
        @input="handleSearch"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <!-- 歌手列表 -->
    <div class="singers-section">
      <h3 class="section-title">歌手</h3>
      <div class="singers-grid">
        <div
          v-for="singer in filteredSingers"
          :key="singer.id"
          class="singer-card"
          @click="loadSingerSongs(singer)"
        >
          <div class="singer-avatar">
            <img :src="singer.picUrl || '/music-server/img/user.jpg'" alt="歌手" />
          </div>
          <div class="singer-name">{{ singer.name }}</div>
        </div>
      </div>
    </div>

    <!-- 歌曲列表 -->
    <div class="songs-section">
      <h3 class="section-title">{{ selectedSinger ? selectedSinger.name + ' 的歌曲' : '所有歌曲' }}</h3>
      <el-table :data="filteredSongs" class="song-table" @row-click="playSong">
        <el-table-column type="index" label="曲目数量" width="150" />
        <el-table-column prop="name" label="歌曲" />
        <el-table-column prop="singerName" label="歌手" width="200" />
        <el-table-column label="" width="100" align="right">
          <template #default="{ row }">
            <el-button
              class="play-btn"
              text
              @click.stop="playSong(row)"
            >
              <img src="/start.svg" alt="播放" class="play-icon" />
              播放
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 音乐播放器 -->
    <MusicPlayer
      v-if="playlist.length > 0"
      :playlist="playlist"
      v-model:currentIndex="currentIndex"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAllSingers, getAllSongs, getSongsBySingerId, searchSongs } from '../api/music'
import MusicPlayer from '../components/MusicPlayer.vue'

const singers = ref([])
const songs = ref([])
const searchKeyword = ref('')
const selectedSinger = ref(null)
const playlist = ref([])
const currentIndex = ref(0)

// 过滤歌手
const filteredSingers = computed(() => {
  if (!searchKeyword.value) return singers.value
  return singers.value.filter(singer =>
    singer.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 过滤歌曲
const filteredSongs = computed(() => {
  if (!searchKeyword.value) return songs.value
  return songs.value.filter(song =>
    song.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    song.singerName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 加载歌手列表
const loadSingers = async () => {
  try {
    const data = await getAllSingers()
    singers.value = data
  } catch (error) {
    ElMessage.error('加载歌手列表失败')
  }
}

// 加载所有歌曲
const loadAllSongs = async () => {
  try {
    const data = await getAllSongs()
    songs.value = data
    playlist.value = data
  } catch (error) {
    ElMessage.error('加载歌曲列表失败')
  }
}

// 加载指定歌手的歌曲
const loadSingerSongs = async (singer) => {
  try {
    selectedSinger.value = singer
    const data = await getSongsBySingerId(singer.id)
    songs.value = data
    playlist.value = data
    if (data.length > 0) {
      currentIndex.value = 0
      ElMessage.success(`已加载 ${singer.name} 的 ${data.length} 首歌曲`)
    } else {
      ElMessage.warning(`${singer.name} 暂无歌曲`)
    }
  } catch (error) {
    ElMessage.error('加载歌手歌曲失败')
  }
}

// 搜索
const handleSearch = async () => {
  if (!searchKeyword.value) {
    selectedSinger.value = null
    await loadAllSongs()
    return
  }

  try {
    const data = await searchSongs(searchKeyword.value)
    songs.value = data
    playlist.value = data
    selectedSinger.value = null
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

// 播放歌曲
const playSong = (song) => {
  const index = playlist.value.findIndex(s => s.id === song.id)
  if (index !== -1) {
    currentIndex.value = index
    ElMessage.success(`正在播放: ${song.name}`)
  }
}

onMounted(() => {
  loadSingers()
  loadAllSongs()
})
</script>

<style scoped>
.music-list {
  padding: 32px 40px;
  padding-bottom: 200px;
  background-color: #121212;
  min-height: 100vh;
  overflow-y: auto;
}

/* 顶部搜索栏 */
.header {
  margin-bottom: 48px;
  display: flex;
  justify-content: center;
}

.search-bar {
  width: 500px;
  max-width: 100%;
}

:deep(.search-bar .el-input__wrapper) {
  background-color: #282828;
  border-radius: 24px;
  box-shadow: none;
  border: none;
  padding: 8px 20px;
}

:deep(.search-bar .el-input__inner) {
  color: #FFFFFF;
  font-size: 14px;
}

:deep(.search-bar .el-input__inner::placeholder) {
  color: #B3B3B3;
}

:deep(.search-bar .el-icon) {
  color: #B3B3B3;
}

/* 分区标题 */
.section-title {
  margin-bottom: 24px;
  color: #FFFFFF;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

/* 歌手区域 */
.singers-section {
  margin-bottom: 56px;
}

.singers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 24px;
}

.singer-card {
  cursor: pointer;
  text-align: center;
  padding: 16px;
  border-radius: 8px;
  background-color: #1E1E1E;
  transition: background-color 0.3s ease;
}

.singer-card:hover {
  background-color: #282828;
}

.singer-avatar {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto 12px;
  border-radius: 50%;
  overflow: hidden;
}

.singer-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: filter 0.3s ease;
}

.singer-card:hover .singer-avatar img {
  filter: brightness(1.2);
}

.singer-name {
  font-size: 15px;
  color: #FFFFFF;
  font-weight: 600;
}

/* 歌曲列表区域 */
.songs-section {
  margin-bottom: 40px;
}

/* ========================================
   核弹级 Element Plus Table 样式覆盖
   强制清除所有 CSS 变量和默认背景色
   ======================================== */

/* 强制重置所有背景相关的 CSS 变量 */
:deep(.song-table),
:deep(.song-table .el-table__inner-wrapper),
:deep(.song-table th),
:deep(.song-table tr),
:deep(.song-table td) {
  background-color: transparent !important;
  --el-table-bg-color: transparent !important;
  --el-table-tr-bg-color: transparent !important;
  --el-table-header-bg-color: transparent !important;
  --el-table-row-hover-bg-color: transparent !important;
  --el-fill-color-lighter: transparent !important;
  border: none !important;
}

/* 表格容器 */
:deep(.song-table) {
  background-color: transparent !important;
}

/* 隐藏表格底部边框线 */
:deep(.song-table .el-table__inner-wrapper::before) {
  display: none !important;
}

/* 表头容器 */
:deep(.song-table .el-table__header-wrapper) {
  background-color: transparent !important;
}

:deep(.song-table .el-table__header) {
  background-color: transparent !important;
}

/* 表头样式 */
:deep(.song-table thead) {
  color: #B3B3B3;
  font-size: 13px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  background-color: transparent !important;
}

:deep(.song-table thead tr) {
  background-color: transparent !important;
}

:deep(.song-table th) {
  background-color: transparent !important;
  padding: 12px 16px;
}

:deep(.song-table th.el-table__cell) {
  background-color: transparent !important;
}

/* 表格主体 */
:deep(.song-table .el-table__body-wrapper) {
  background-color: transparent !important;
}

:deep(.song-table tbody) {
  background-color: transparent !important;
}

/* 表格行 - 默认状态 */
:deep(.song-table .el-table__row) {
  background-color: transparent !important;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

/* 表格行 - 悬停状态（简单深灰色，确保文字清晰） */
:deep(.song-table .el-table__row:hover) {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

:deep(.song-table .el-table__row:hover td) {
  background-color: transparent !important;
}

:deep(.song-table .el-table__row:hover .cell) {
  color: #FFFFFF !important;
}

/* 单元格 */
:deep(.song-table td) {
  background-color: transparent !important;
  padding: 12px 16px;
}

:deep(.song-table td.el-table__cell) {
  background-color: transparent !important;
  border-bottom: 1px solid #282828 !important;
}

:deep(.song-table .el-table__row:last-child td) {
  border-bottom: none !important;
}

/* 单元格文字颜色 */
:deep(.song-table .cell) {
  color: #FFFFFF;
  font-size: 14px;
}

:deep(.song-table td:nth-child(1) .cell) {
  color: #B3B3B3;
  font-size: 14px;
}

:deep(.song-table td:nth-child(3) .cell) {
  color: #B3B3B3;
  font-size: 14px;
}

/* 播放按钮 */
.play-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #1db954;
  font-weight: 600;
  font-size: 13px;
  padding: 6px 12px;
  opacity: 0;
  transition: opacity 0.2s ease;
  /* 核心修复：强制去除背景和边框 */
  background-color: transparent !important;
  border: none !important;
}

.play-icon {
  width: 14px;
  height: 14px;
  filter: brightness(0) saturate(100%) invert(64%) sepia(98%) saturate(2176%) hue-rotate(95deg) brightness(96%) contrast(85%);
}

:deep(.song-table .el-table__row:hover) .play-btn {
  opacity: 1;
}

/* 覆盖 Element Plus 默认的 hover/focus 背景变量 */
.play-btn:hover,
.play-btn:focus,
.play-btn:active {
  color: #1ed760;
  background-color: transparent !important;
}

.play-btn:hover .play-icon {
  filter: brightness(0) saturate(100%) invert(75%) sepia(50%) saturate(2500%) hue-rotate(95deg) brightness(100%) contrast(90%);
}
</style>
