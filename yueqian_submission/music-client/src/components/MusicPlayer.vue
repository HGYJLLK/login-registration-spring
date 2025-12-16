<template>
  <div class="music-player">
    <div class="player-container">
      <!-- 歌曲信息 -->
      <div class="song-info">
        <img :src="getSongPic(currentSong?.picUrl)" alt="封面" class="cover" @error="handleImageError" />
        <div class="info">
          <div class="song-name">{{ currentSong?.name || '未播放' }}</div>
          <div class="singer-name">{{ currentSong ? getSingerName(currentSong.singerId) : '选择一首歌曲开始播放' }}</div>
        </div>
      </div>

      <!-- 播放控制 -->
      <div class="controls">
        <el-icon class="control-btn" :size="28" @click="playPrevious">
          <ArrowLeftBold />
        </el-icon>
        <el-icon class="control-btn play-pause-btn" :size="36" @click="togglePlay">
          <VideoPlay v-if="!isPlaying" />
          <VideoPause v-else />
        </el-icon>
        <el-icon class="control-btn" :size="28" @click="playNext">
          <ArrowRightBold />
        </el-icon>
      </div>

      <!-- 进度条 -->
      <div class="progress-container">
        <span class="time">{{ formatTime(currentTime) }}</span>
        <el-slider
          v-model="currentTime"
          :max="duration"
          :show-tooltip="false"
          @change="handleProgressChange"
          class="progress-bar"
        />
        <span class="time">{{ formatTime(duration) }}</span>
      </div>

      <!-- 音量控制 -->
      <div class="volume-control">
        <el-icon @click="toggleMute" class="volume-icon">
          <Mute v-if="volume === 0" />
          <Microphone v-else />
        </el-icon>
        <el-slider v-model="volume" :show-tooltip="false" style="width: 100px" @input="handleVolumeChange" />
      </div>

      <!-- 音频元素 -->
      <audio
        ref="audioRef"
        @timeupdate="updateProgress"
        @ended="handleEnded"
        @loadedmetadata="updateDuration"
        @play="isPlaying = true"
        @pause="isPlaying = false"
      ></audio>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onBeforeUnmount } from 'vue'
import { VideoPlay, VideoPause, Microphone, Mute, ArrowLeftBold, ArrowRightBold } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  song: {
    type: Object,
    default: null
  },
  singers: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['play-next', 'play-previous'])

const audioRef = ref(null)
const currentSong = ref(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(50)
const lastVolume = ref(50)

const getSongPic = (pic) => {
  if (!pic) return '/music.svg'
  if (pic.startsWith('http')) return pic
  return `http://localhost:8082${pic}`
}

const handleImageError = (e) => {
  e.target.src = '/music.svg'
}

const getSingerName = (singerId) => {
  const singer = props.singers.find(s => s.id === singerId)
  return singer ? singer.name : '未知歌手'
}

// 播放/暂停
const togglePlay = () => {
  if (!audioRef.value) return
  if (isPlaying.value) {
    audioRef.value.pause()
  } else {
    audioRef.value.play()
  }
}

// 更新进度
const updateProgress = () => {
  if (audioRef.value) {
    currentTime.value = audioRef.value.currentTime
  }
}

// 更新总时长
const updateDuration = () => {
  if (audioRef.value) {
    duration.value = audioRef.value.duration
  }
}

// 拖动进度条
const handleProgressChange = (value) => {
  if (audioRef.value) {
    audioRef.value.currentTime = value
  }
}

// 音量变化
const handleVolumeChange = () => {
  if (audioRef.value) {
    audioRef.value.volume = volume.value / 100
  }
}

// 静音/取消静音
const toggleMute = () => {
  if (volume.value === 0) {
    volume.value = lastVolume.value || 50
  } else {
    lastVolume.value = volume.value
    volume.value = 0
  }
}

// 播放结束
const handleEnded = () => {
  isPlaying.value = false
  currentTime.value = 0
}

// 上一首
const playPrevious = () => {
  emit('play-previous')
}

// 下一首
const playNext = () => {
  emit('play-next')
}

// 关闭播放器
const closePlayer = () => {
  if (audioRef.value) {
    audioRef.value.pause()
    audioRef.value.currentTime = 0
  }
  currentSong.value = null
  isPlaying.value = false
}

// 监听音量变化
watch(volume, (newVolume) => {
  if (audioRef.value) {
    audioRef.value.volume = newVolume / 100
  }
})

// 监听歌曲变化
watch(() => props.song, (newSong) => {
  if (newSong) {
    // 停止当前播放
    if (audioRef.value) {
      audioRef.value.pause()
      audioRef.value.currentTime = 0
    }

    currentSong.value = newSong
    const songUrl = newSong.songUrl || newSong.url
    if (!songUrl) {
      ElMessage.warning('该歌曲暂无音频文件')
      return
    }

    const fullUrl = songUrl.startsWith('http') ? songUrl : `http://localhost:8082${songUrl}`

    if (audioRef.value) {
      audioRef.value.src = fullUrl
      audioRef.value.volume = volume.value / 100
      audioRef.value.play().catch(error => {
        console.error('播放失败', error)
        ElMessage.error('播放失败')
      })
    }
  }
}, { immediate: true })

onBeforeUnmount(() => {
  if (audioRef.value) {
    audioRef.value.pause()
    audioRef.value.src = ''
  }
})

// 格式化时间
const formatTime = (seconds) => {
  if (!seconds || isNaN(seconds)) return '0:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return mins + ':' + (secs < 10 ? '0' : '') + secs
}
</script>

<style scoped>
.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #181818;
  border-top: 1px solid #282828;
  color: white;
  padding: 15px 30px;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.player-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
  gap: 20px;
}

.song-info {
  display: flex;
  align-items: center;
  gap: 15px;
  min-width: 250px;
}

.cover {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.info {
  overflow: hidden;
}

.song-name {
  font-weight: 600;
  font-size: 14px;
  color: #FFFFFF;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.singer-name {
  font-size: 12px;
  color: #B3B3B3;
  margin-top: 4px;
}

.controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.progress-container {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 15px;
  max-width: 600px;
}

.time {
  font-size: 12px;
  min-width: 40px;
}

.progress-bar {
  flex: 1;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 150px;
}

.el-icon {
  cursor: pointer;
  font-size: 20px;
}

.el-icon:hover {
  opacity: 0.8;
}

.play-icon-svg {
  width: 32px;
  height: 32px;
  filter: brightness(0) invert(1);
}

/* 进度条样式 */
:deep(.el-slider__runway) {
  background-color: #404040;
  height: 4px;
}

:deep(.el-slider__bar) {
  background-color: #1db954;
}

:deep(.el-slider__button) {
  width: 12px;
  height: 12px;
  border: 2px solid #FFFFFF;
  background-color: #FFFFFF;
}

:deep(.el-slider__button:hover) {
  transform: scale(1.2);
}

/* 音量控制滑块样式 */
:deep(.volume-control .el-slider__runway) {
  background-color: #404040;
  height: 4px;
}

:deep(.volume-control .el-slider__bar) {
  background-color: #FFFFFF;
}

:deep(.volume-control .el-slider__button) {
  width: 12px;
  height: 12px;
  border: 2px solid #FFFFFF;
  background-color: #FFFFFF;
}
</style>
