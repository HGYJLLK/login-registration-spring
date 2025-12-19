<template>
  <div class="songs-container">
    <div class="page-header">
      <h2>{{ pageTitle }}</h2>
      <div class="header-actions">
        <el-select
          v-model="selectedStyle"
          placeholder="筛选风格"
          clearable
          @change="handleStyleChange"
          style="width: 150px; margin-right: 10px"
        >
          <el-option label="全部风格" value="" />
          <el-option v-for="style in styleList" :key="style" :label="style" :value="style" />
        </el-select>
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
          <div class="song-info" @click="showSongDetail(row)" style="cursor: pointer">
            <div class="song-name">{{ row.name }}</div>
            <div class="singer-name-sub">{{ getSingerName(row.singerId) }}</div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="评分" width="120" align="center">
        <template #default="{ row }">
          <div class="rating-display">
            <el-rate
              :model-value="getRating(row)"
              disabled
              size="small"
            />
            <span class="rating-text">{{ formatRating(row.averageRating) }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="点赞数" width="100" align="center">
        <template #default="{ row }">
          <div class="like-count">
            <el-icon><Pointer /></el-icon>
            <span>{{ row.totalLikes || 0 }}</span>
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

    <!-- 歌曲详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      :title="currentSongDetail?.name"
      width="700px"
      class="song-detail-dialog"
    >
      <div v-if="currentSongDetail" class="song-detail-content">
        <!-- 歌曲基本信息 -->
        <div class="detail-header">
          <img
            :src="getSongPic(currentSongDetail.picUrl)"
            class="detail-cover"
            @error="handleImageError"
          />
          <div class="detail-info">
            <h3>{{ currentSongDetail.name }}</h3>
            <p class="singer-name">歌手: {{ currentSongDetail.singerName }}</p>
            <p v-if="currentSongDetail.style" class="song-style">风格: {{ currentSongDetail.style }}</p>

            <!-- 评分和点赞统计 -->
            <div class="stats-row">
              <div class="stat-item">
                <el-rate
                  :model-value="currentSongRating"
                  disabled
                  show-score
                  text-color="#ff9900"
                />
                <span class="stat-text">{{ formatRating(currentSongDetail.averageRating) }} / 5.0</span>
              </div>
              <div class="stat-item">
                <el-icon class="stat-icon"><Comment /></el-icon>
                <span class="stat-text">{{ currentSongDetail.commentCount || 0 }} 评论</span>
              </div>
              <div class="stat-item">
                <el-icon class="stat-icon"><Pointer /></el-icon>
                <span class="stat-text">{{ currentSongDetail.totalLikes || 0 }} 点赞</span>
              </div>
            </div>

            <!-- 点赞按钮 -->
            <el-button type="primary" @click="likeSongInDetail" class="like-button">
              <el-icon><Pointer /></el-icon>
              点赞
            </el-button>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comments-section">
          <h4>评论列表</h4>

          <!-- 添加评论表单 -->
          <div class="add-comment-form">
            <el-form :model="newComment" label-width="80px">
              <el-form-item label="评分">
                <el-rate v-model="newComment.rating" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" />
                <span class="rating-hint">请选择1-5星</span>
              </el-form-item>
              <el-form-item label="评论内容">
                <el-input
                  v-model="newComment.content"
                  type="textarea"
                  :rows="3"
                  placeholder="说说你的感受..."
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitComment">提交评论</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div v-if="comments.length === 0" class="no-comments">
              暂无评论，快来发表第一条评论吧！
            </div>
            <div v-else>
              <div
                v-for="comment in comments"
                :key="comment.id"
                class="comment-item"
              >
                <div class="comment-header">
                  <img
                    :src="getUserAvatar(comment.avatarUrl)"
                    class="user-avatar"
                    @error="handleAvatarError"
                  />
                  <div class="comment-user-info">
                    <span class="comment-username">{{ comment.username }}</span>
                    <el-rate
                      :model-value="comment.rating"
                      disabled
                      size="small"
                    />
                  </div>
                  <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
                <div class="comment-actions">
                  <el-button
                    text
                    :type="comment.isLiked ? 'primary' : ''"
                    @click="toggleCommentLike(comment)"
                  >
                    <el-icon><Pointer /></el-icon>
                    {{ comment.likeCount || 0 }}
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, inject } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Star, StarFilled, VideoPlay, Pointer, Comment } from '@element-plus/icons-vue'
import axios from 'axios'

const route = useRoute()
const songs = ref([])
const singers = ref([])
const searchText = ref('')
const userFavorites = ref([])
const userId = ref(null)
const playSongHandler = inject('playSong')
const setPlaylistHandler = inject('setPlaylist')

// 新增：风格筛选
const styleList = ref([])
const selectedStyle = ref('')

// 新增：歌曲详情对话框
const detailDialogVisible = ref(false)
const currentSongDetail = ref(null)
const currentSongRating = ref(0)
const comments = ref([])
const newComment = ref({
  rating: 5,
  content: ''
})

const pageTitle = computed(() => {
  if (route.query.singerName) {
    return `${route.query.singerName} 的歌曲`
  }
  return '所有歌曲'
})

const filteredSongs = computed(() => {
  let result = songs.value

  // 风格筛选
  if (selectedStyle.value) {
    result = result.filter(song => song.style === selectedStyle.value)
  }

  // 搜索筛选
  if (searchText.value) {
    const keyword = searchText.value.toLowerCase()
    result = result.filter(song => {
      const singerName = getSingerName(song.singerId).toLowerCase()
      return song.name.toLowerCase().includes(keyword) ||
        singerName.includes(keyword)
    })
  }

  return result
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

const handleStyleChange = () => {
  // Style filter is handled by computed property
}

const playSong = (song) => {
  if (playSongHandler) {
    playSongHandler(song)
    ElMessage.success(`正在播放: ${song.name}`)
  }
}

const likeSong = async (song) => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    await axios.post('http://localhost:8082/api/music/song/like', {
      userId: storedUserId,
      songId: song.id
    })
    ElMessage.success(`已点赞: ${song.name}`)
    // 刷新歌曲列表以更新点赞数
    await loadSongs()
  } catch (error) {
    console.error('点赞失败', error)
    ElMessage.error('点赞失败')
  }
}

// 新增：显示歌曲详情对话框
const showSongDetail = async (song) => {
  const storedUserId = localStorage.getItem('userId')

  try {
    // 加载歌曲详细信息（带统计）
    const songRes = await axios.get('http://localhost:8082/api/music/song/detail-with-stats', {
      params: { id: song.id }
    })
    currentSongDetail.value = {
      ...songRes.data,
      singerName: getSingerName(songRes.data.singerId)
    }
    currentSongRating.value = songRes.data.averageRating || 0

    // 加载评论列表
    const commentsRes = await axios.get('http://localhost:8082/api/music/comment/list', {
      params: {
        songId: song.id,
        userId: storedUserId || 0
      }
    })
    comments.value = commentsRes.data

    // 重置评论表单
    newComment.value = {
      rating: 5,
      content: ''
    }

    detailDialogVisible.value = true
  } catch (error) {
    console.error('加载歌曲详情失败', error)
    ElMessage.error('加载歌曲详情失败')
  }
}

// 新增：提交评论
const submitComment = async () => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  if (!newComment.value.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  if (!newComment.value.rating || newComment.value.rating < 1 || newComment.value.rating > 5) {
    ElMessage.warning('请选择1-5星的评分')
    return
  }

  try {
    await axios.post('http://localhost:8082/api/music/comment/add', {
      userId: storedUserId,
      songId: currentSongDetail.value.id,
      content: newComment.value.content,
      rating: newComment.value.rating
    })
    ElMessage.success('评论提交成功')

    // 重新加载歌曲详情和评论
    await showSongDetail(currentSongDetail.value)
    // 刷新主列表以更新评分
    await loadSongs()
  } catch (error) {
    console.error('提交评论失败', error)
    ElMessage.error('提交评论失败')
  }
}

// 新增：详情页点赞歌曲
const likeSongInDetail = async () => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    await axios.post('http://localhost:8082/api/music/song/like', {
      userId: storedUserId,
      songId: currentSongDetail.value.id
    })
    ElMessage.success('点赞成功')

    // 刷新详情和主列表
    await showSongDetail(currentSongDetail.value)
    await loadSongs()
  } catch (error) {
    console.error('点赞失败', error)
    ElMessage.error('点赞失败')
  }
}

// 新增：切换评论点赞
const toggleCommentLike = async (comment) => {
  const storedUserId = localStorage.getItem('userId')
  if (!storedUserId) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    await axios.post('http://localhost:8082/api/music/comment/like', {
      userId: storedUserId,
      commentId: comment.id
    })

    // 刷新评论列表
    await showSongDetail(currentSongDetail.value)
  } catch (error) {
    console.error('评论点赞失败', error)
    ElMessage.error('操作失败')
  }
}

// 新增：格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''

  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (seconds < 60) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  // 超过7天显示具体日期
  return time.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 新增：获取评分
const getRating = (song) => {
  return song.averageRating || 0
}

// 新增：格式化评分
const formatRating = (rating) => {
  if (!rating) return '0.0'
  return rating.toFixed(1)
}

// 新增：获取用户头像
const getUserAvatar = (avatarUrl) => {
  if (!avatarUrl) return '/default-avatar.png'
  if (avatarUrl.startsWith('http')) return avatarUrl
  return `http://localhost:8082${avatarUrl}`
}

// 新增：头像加载失败处理
const handleAvatarError = (e) => {
  e.target.src = '/default-avatar.png'
}

const loadSongs = async () => {
  try {
    let res
    if (route.query.singerId) {
      // 按歌手查询时，仍使用原API，然后手动填充统计信息
      res = await axios.get('http://localhost:8082/api/music/song/singer/detail', {
        params: { singerId: route.query.singerId }
      })
      // 为每首歌曲获取统计信息
      const songsWithStats = await Promise.all(
        res.data.map(async (song) => {
          try {
            const statsRes = await axios.get('http://localhost:8082/api/music/song/detail-with-stats', {
              params: { id: song.id }
            })
            return statsRes.data
          } catch (error) {
            console.error(`加载歌曲${song.id}统计失败`, error)
            return { ...song, averageRating: 0, commentCount: 0, totalLikes: 0 }
          }
        })
      )
      songs.value = songsWithStats
    } else {
      // 查询所有歌曲，使用带统计的API
      res = await axios.get('http://localhost:8082/api/music/song/all-with-stats')
      songs.value = res.data
    }
    // 更新播放列表
    if (setPlaylistHandler) {
      setPlaylistHandler(songs.value)
    }
  } catch (error) {
    console.error('加载歌曲失败', error)
    ElMessage.error('加载歌曲失败')
  }
}

// 新增：加载风格列表
const loadStyles = async () => {
  try {
    const res = await axios.get('http://localhost:8082/api/music/song/styles')
    styleList.value = res.data
  } catch (error) {
    console.error('加载风格列表失败', error)
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
  loadStyles()
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

/* 新增样式：评分显示 */
.rating-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.rating-text {
  font-size: 12px;
  color: #B3B3B3;
}

/* 新增样式：点赞数显示 */
.like-count {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #B3B3B3;
}

/* 歌曲详情对话框样式 */
:deep(.song-detail-dialog) {
  background-color: #181818;
}

:deep(.song-detail-dialog .el-dialog__header) {
  background-color: #181818;
  border-bottom: 1px solid #282828;
}

:deep(.song-detail-dialog .el-dialog__title) {
  color: #FFFFFF;
  font-size: 20px;
  font-weight: 600;
}

:deep(.song-detail-dialog .el-dialog__body) {
  background-color: #181818;
  color: #FFFFFF;
  padding: 24px;
}

.song-detail-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 详情头部 */
.detail-header {
  display: flex;
  gap: 20px;
  padding-bottom: 24px;
  border-bottom: 1px solid #282828;
}

.detail-cover {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.detail-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-info h3 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #FFFFFF;
}

.singer-name {
  color: #B3B3B3;
  font-size: 14px;
  margin: 0;
}

.song-style {
  color: #1db954;
  font-size: 14px;
  margin: 0;
}

.stats-row {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-icon {
  color: #1db954;
}

.stat-text {
  color: #B3B3B3;
  font-size: 14px;
}

.like-button {
  margin-top: 8px;
  align-self: flex-start;
}

/* 评论区 */
.comments-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comments-section h4 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #FFFFFF;
}

/* 添加评论表单 */
.add-comment-form {
  background-color: #282828;
  padding: 20px;
  border-radius: 8px;
}

:deep(.add-comment-form .el-form-item__label) {
  color: #FFFFFF;
}

:deep(.add-comment-form .el-textarea__inner) {
  background-color: #181818;
  border-color: #404040;
  color: #FFFFFF;
}

:deep(.add-comment-form .el-textarea__inner::placeholder) {
  color: #B3B3B3;
}

.rating-hint {
  margin-left: 12px;
  color: #B3B3B3;
  font-size: 13px;
}

/* 评论列表 */
.comments-list {
  max-height: 400px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comments-list::-webkit-scrollbar {
  width: 8px;
}

.comments-list::-webkit-scrollbar-track {
  background: #181818;
}

.comments-list::-webkit-scrollbar-thumb {
  background: #404040;
  border-radius: 4px;
}

.comments-list::-webkit-scrollbar-thumb:hover {
  background: #505050;
}

.no-comments {
  text-align: center;
  padding: 40px 20px;
  color: #B3B3B3;
  font-size: 14px;
}

.comment-item {
  background-color: #282828;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-user-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
}

.comment-username {
  font-weight: 500;
  color: #FFFFFF;
  font-size: 14px;
}

.comment-time {
  color: #B3B3B3;
  font-size: 12px;
  margin-left: auto;
}

.comment-content {
  color: #E0E0E0;
  font-size: 14px;
  line-height: 1.6;
  padding-left: 52px;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-left: 52px;
}

:deep(.comment-actions .el-button) {
  color: #B3B3B3;
}

:deep(.comment-actions .el-button.is-text:hover) {
  color: #1db954;
}

:deep(.comment-actions .el-button--primary) {
  color: #1db954;
}

/* Element Plus 组件深色主题覆盖 */
:deep(.el-select .el-input__wrapper) {
  background-color: #282828;
}

:deep(.el-select .el-input__inner) {
  color: #FFFFFF;
}

:deep(.el-select-dropdown) {
  background-color: #282828;
  border-color: #404040;
}

:deep(.el-select-dropdown__item) {
  color: #FFFFFF;
}

:deep(.el-select-dropdown__item:hover) {
  background-color: #404040;
}

:deep(.el-select-dropdown__item.selected) {
  color: #1db954;
}

:deep(.el-rate) {
  --el-rate-fill-color: #FF9900;
}

:deep(.el-dialog__close) {
  color: #B3B3B3;
}

:deep(.el-dialog__close:hover) {
  color: #FFFFFF;
}
</style>
