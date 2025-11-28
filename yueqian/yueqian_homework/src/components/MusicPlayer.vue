<template>
  <div class="music-player" v-if="currentSong">
    <div class="player-container">
      <!-- 歌曲信息 -->
      <div class="song-info">
        <img :src="currentSong.picUrl || '/music-server/img/tubiao.jpg'" alt="封面" class="cover" />
        <div class="info">
          <div class="song-name">{{ currentSong.name }}</div>
          <div class="singer-name">{{ currentSong.singerName }}</div>
        </div>
      </div>

      <!-- 播放控制 -->
      <div class="controls">
        <el-button @click="prev" :icon="Back" circle />
        <el-button @click="togglePlay" circle type="primary" size="large" class="play-control-btn">
          <img v-if="!isPlaying" src="/start.svg" alt="播放" class="play-icon-svg" />
          <el-icon v-else><VideoPause /></el-icon>
        </el-button>
        <el-button @click="next" :icon="Right" circle />
      </div>

      <!-- 进度条 -->
      <div class="progress-container">
        <span class="time">{{ formatTime(currentTime) }}</span>
        <el-slider
          v-model="progress"
          :show-tooltip="false"
          @change="handleProgressChange"
          class="progress-bar"
        />
        <span class="time">{{ formatTime(duration) }}</span>
      </div>

      <!-- 音量控制 -->
      <div class="volume-control">
        <el-icon @click="toggleMute">
          <component :is="volume === 0 ? Mute : Microphone" />
        </el-icon>
        <el-slider v-model="volume" :show-tooltip="false" style="width: 100px" />
      </div>

      <!-- 音频元素 -->
      <audio
        ref="audioRef"
        :src="currentSong.songUrl"
        @timeupdate="updateProgress"
        @ended="next"
        @loadedmetadata="updateDuration"
      ></audio>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Back, Right, VideoPause, Microphone, Mute } from '@element-plus/icons-vue'

const props = defineProps({
  playlist: {
    type: Array,
    default: () => []
  },
  currentIndex: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update:currentIndex'])

const audioRef = ref(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(50)
const progress = ref(0)
const lastVolume = ref(50)

const currentSong = computed(() => props.playlist[props.currentIndex])

// 播放/暂停
const togglePlay = () => {
  if (isPlaying.value) {
    audioRef.value.pause()
  } else {
    audioRef.value.play()
  }
  isPlaying.value = !isPlaying.value
}

// 上一首
const prev = () => {
  let newIndex = props.currentIndex - 1
  if (newIndex < 0) {
    newIndex = props.playlist.length - 1
  }
  emit('update:currentIndex', newIndex)
  setTimeout(() => {
    audioRef.value.play()
    isPlaying.value = true
  }, 100)
}

// 下一首
const next = () => {
  let newIndex = props.currentIndex + 1
  if (newIndex >= props.playlist.length) {
    newIndex = 0
  }
  emit('update:currentIndex', newIndex)
  setTimeout(() => {
    audioRef.value.play()
    isPlaying.value = true
  }, 100)
}

// 更新进度
const updateProgress = () => {
  currentTime.value = audioRef.value.currentTime
  progress.value = (currentTime.value / duration.value) * 100
}

// 更新总时长
const updateDuration = () => {
  duration.value = audioRef.value.duration
}

// 拖动进度条
const handleProgressChange = (value) => {
  const newTime = (value / 100) * duration.value
  audioRef.value.currentTime = newTime
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

// 监听音量变化
watch(volume, (newVolume) => {
  if (audioRef.value) {
    audioRef.value.volume = newVolume / 100
  }
})

// 监听歌曲变化
watch(() => props.currentIndex, () => {
  setTimeout(() => {
    if (isPlaying.value) {
      audioRef.value.play()
    }
  }, 100)
})

// 格式化时间
const formatTime = (seconds) => {
  if (!seconds || isNaN(seconds)) return '00:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
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
