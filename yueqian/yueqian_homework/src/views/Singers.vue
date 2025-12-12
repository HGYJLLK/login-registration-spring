<template>
  <div class="singers-container">
    <div class="page-header">
      <h2>歌手</h2>
      <el-input
        v-model="searchText"
        placeholder="搜索歌手..."
        style="width: 300px"
        clearable
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <div class="singers-grid">
      <div
        v-for="singer in filteredSingers"
        :key="singer.id"
        class="singer-card"
        @click="viewSingerSongs(singer)"
      >
        <div class="singer-pic">
          <img
            :src="getSingerPic(singer.picUrl)"
            :alt="singer.name"
            @error="handleImageError"
          />
        </div>
        <div class="singer-info">
          <div class="singer-name">{{ singer.name }}</div>
          <div class="singer-style">华语</div>
        </div>
      </div>
    </div>

    <el-empty v-if="filteredSingers.length === 0" description="没有找到歌手" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const singers = ref([])
const searchText = ref('')

const filteredSingers = computed(() => {
  if (!searchText.value) {
    return singers.value
  }
  const keyword = searchText.value.toLowerCase()
  return singers.value.filter(singer =>
    singer.name.toLowerCase().includes(keyword)
  )
})

const getSingerPic = (pic) => {
  if (!pic) return '/music.svg'
  if (pic.startsWith('http')) return pic
  return `http://localhost:8082${pic}`
}

const handleImageError = (e) => {
  e.target.src = '/music.svg'
}

const handleSearch = () => {
  // Search is handled by computed property
}

const viewSingerSongs = (singer) => {
  router.push({
    path: '/home/songs',
    query: { singerId: singer.id, singerName: singer.name }
  })
}

const loadSingers = async () => {
  try {
    const res = await axios.get('http://localhost:8082/api/music/singer')
    singers.value = res.data
  } catch (error) {
    console.error('加载歌手失败', error)
    ElMessage.error('加载歌手失败')
  }
}

onMounted(() => {
  loadSingers()
})
</script>

<style scoped>
.singers-container {
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

.singers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 24px;
}

.singer-card {
  background-color: #181818;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.singer-card:hover {
  background-color: #282828;
}

.singer-pic {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 12px;
  background-color: #282828;
}

.singer-pic img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.singer-info {
  text-align: center;
}

.singer-name {
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.singer-style {
  font-size: 14px;
  color: #B3B3B3;
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
