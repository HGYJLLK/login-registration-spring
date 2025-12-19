<template>
  <div class="admin-container">
    <el-container class="layout-container">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <div class="header-left">
          <img src="/music.svg" alt="音乐" class="header-icon" />
          <span>音乐管理系统 - 管理员</span>
        </div>
        <div class="header-right">
          <el-button type="danger" size="small" @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <el-container>
        <!-- 左侧导航菜单 -->
        <el-aside width="200px" class="aside">
          <el-menu
            :default-active="activeMenu"
            @select="handleMenuSelect"
            background-color="#000000"
            text-color="#B3B3B3"
            active-text-color="#1db954"
          >
            <el-menu-item index="consumer">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="singer">
              <el-icon><Microphone /></el-icon>
              <span>歌手管理</span>
            </el-menu-item>
            <el-menu-item index="song">
              <el-icon><Headset /></el-icon>
              <span>音乐管理</span>
            </el-menu-item>
            <el-menu-item index="songlist">
              <el-icon><Menu /></el-icon>
              <span>歌单管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 主内容区域 -->
        <el-main class="main">
          <!-- 用户管理模块 -->
          <div v-if="activeMenu === 'consumer'">
            <h3>用户管理</h3>
            <el-table :data="users" stripe style="width: 100%" max-height="500">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column label="头像" width="100">
                <template #default="scope">
                  <img v-if="scope.row.avatarUrl" :src="getFileUrl(scope.row.avatarUrl)" class="avatar-img">
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="password" label="密码" width="200" show-overflow-tooltip>
                <template #default="scope">
                  <span style="font-family: monospace; font-size: 12px;">{{ scope.row.password }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="姓名" width="120" />
              <el-table-column prop="gender" label="性别" width="80" />
              <el-table-column prop="phonenumber" label="手机号" width="140" />
              <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 歌手管理模块 -->
          <div v-if="activeMenu === 'singer'">
            <h3>歌手管理</h3>
            <el-button type="primary" @click="addSinger" style="margin-bottom: 20px">添加歌手</el-button>
            <el-table :data="singers" stripe style="width: 100%" max-height="500">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column label="图片" width="100">
                <template #default="scope">
                  <img v-if="scope.row.picUrl" :src="getFileUrl(scope.row.picUrl)" class="avatar-img">
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="歌手名" width="150" />
              <el-table-column prop="introduction" label="简介" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" @click="editSinger(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteSinger(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 音乐管理模块 -->
          <div v-if="activeMenu === 'song'">
            <h3>音乐管理</h3>
            <el-button type="primary" @click="addSong" style="margin-bottom: 20px">添加音乐</el-button>
            <el-table :data="songs" stripe style="width: 100%" max-height="500">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column label="封面" width="100">
                <template #default="scope">
                  <img v-if="scope.row.picUrl" :src="getFileUrl(scope.row.picUrl)" class="avatar-img">
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="歌曲名" width="150" />
              <el-table-column prop="singerName" label="歌手" width="120" />
              <el-table-column prop="style" label="风格" width="100" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" @click="editSong(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteSong(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 歌单管理模块 -->
          <div v-if="activeMenu === 'songlist'">
            <h3>歌单管理</h3>
            <el-button type="primary" @click="addSongList" style="margin-bottom: 20px">添加歌单</el-button>
            <el-table :data="songLists" stripe style="width: 100%" max-height="500">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column label="封面" width="100">
                <template #default="scope">
                  <img v-if="scope.row.picUrl" :src="getFileUrl(scope.row.picUrl)" class="avatar-img">
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="歌单名" width="200" />
              <el-table-column prop="style" label="风格" width="120" />
              <el-table-column prop="introduction" label="简介" />
              <el-table-column label="操作" width="300">
                <template #default="scope">
                  <el-button size="small" @click="editSongList(scope.row)">编辑</el-button>
                  <el-button size="small" type="primary" @click="manageSongs(scope.row)">管理歌曲</el-button>
                  <el-button size="small" type="danger" @click="deleteSongList(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 用户编辑对话框 -->
    <el-dialog v-model="userDialogVisible" title="编辑用户" width="500px">
      <el-form :model="currentUser" label-width="100px">
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8082/api/file/avatar"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="currentUser.avatarUrl" :src="getFileUrl(currentUser.avatarUrl)" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="currentUser.username" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="currentUser.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="currentUser.gender" placeholder="请选择">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="currentUser.phonenumber" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="currentUser.email" type="email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="currentUser.password" type="password" placeholder="不修改请留空" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>

    <!-- 歌手编辑对话框 -->
    <el-dialog v-model="singerDialogVisible" :title="singerDialogTitle" width="500px">
      <el-form :model="currentSinger" label-width="100px">
        <el-form-item label="歌手图片">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8082/api/file/singer-pic"
            :show-file-list="false"
            :on-success="handleSingerPicSuccess"
          >
            <img v-if="currentSinger.picUrl" :src="getFileUrl(currentSinger.picUrl)" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌手名">
          <el-input v-model="currentSinger.name" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" v-model="currentSinger.introduction" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="singerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSinger">保存</el-button>
      </template>
    </el-dialog>

    <!-- 音乐编辑对话框 -->
    <el-dialog v-model="songDialogVisible" :title="songDialogTitle" width="600px">
      <el-form :model="currentSong" label-width="100px">
        <el-form-item label="歌曲封面">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8082/api/file/song-pic"
            :show-file-list="false"
            :on-success="handleSongPicSuccess"
          >
            <img v-if="currentSong.picUrl" :src="getFileUrl(currentSong.picUrl)" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌曲名">
          <el-input v-model="currentSong.name" />
        </el-form-item>
        <el-form-item label="歌手">
          <el-select v-model="currentSong.singerId" placeholder="请选择歌手" @change="handleSingerChange">
            <el-option v-for="singer in singers" :key="singer.id" :label="singer.name" :value="singer.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="歌曲风格">
          <el-select v-model="currentSong.style" placeholder="请选择或输入风格" allow-create filterable>
            <el-option label="流行" value="流行" />
            <el-option label="摇滚" value="摇滚" />
            <el-option label="民谣" value="民谣" />
            <el-option label="古典" value="古典" />
            <el-option label="电子" value="电子" />
            <el-option label="爵士" value="爵士" />
            <el-option label="嘻哈" value="嘻哈" />
            <el-option label="R&B" value="R&B" />
          </el-select>
        </el-form-item>
        <el-form-item label="音乐文件">
          <el-upload
            action="http://localhost:8082/api/file/song"
            :on-success="handleSongFileSuccess"
            :file-list="songFileList"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">只能上传mp3文件</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌词">
          <el-input type="textarea" v-model="currentSong.lyric" :rows="4" />
        </el-form-item>
        <el-form-item label="时长(秒)">
          <el-input-number v-model="currentSong.duration" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="songDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSong">保存</el-button>
      </template>
    </el-dialog>

    <!-- 歌单编辑对话框 -->
    <el-dialog v-model="songListDialogVisible" :title="songListDialogTitle" width="500px">
      <el-form :model="currentSongList" label-width="100px">
        <el-form-item label="歌单封面">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8082/api/file/song-pic"
            :show-file-list="false"
            :on-success="handleSongListPicSuccess"
          >
            <img v-if="currentSongList.picUrl" :src="getFileUrl(currentSongList.picUrl)" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="歌单名">
          <el-input v-model="currentSongList.title" />
        </el-form-item>
        <el-form-item label="风格">
          <el-input v-model="currentSongList.style" placeholder="如: 流行、摇滚、古典等" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" v-model="currentSongList.introduction" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="songListDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSongList">保存</el-button>
      </template>
    </el-dialog>

    <!-- 歌单歌曲管理对话框 -->
    <el-dialog v-model="songListSongsDialogVisible" :title="`管理歌单: ${currentSongList.title}`" width="800px">
      <div>
        <h4>添加歌曲到歌单</h4>
        <el-select v-model="selectedSongId" placeholder="选择要添加的歌曲" style="width: 300px; margin-right: 10px;">
          <el-option v-for="song in songs" :key="song.id" :label="`${song.name} - ${song.singerName}`" :value="song.id" />
        </el-select>
        <el-button type="primary" @click="addSongToCurrentList">添加</el-button>

        <h4 style="margin-top: 20px;">歌单中的歌曲</h4>
        <el-table :data="currentSongListSongs" stripe style="width: 100%" max-height="300">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="歌曲名" width="200" />
          <el-table-column prop="singerName" label="歌手" width="150" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="danger" @click="removeSongFromCurrentList(scope.row.id)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="songListSongsDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Microphone, Headset, Menu, Plus } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const API_BASE = 'http://localhost:8082/api'

const activeMenu = ref('consumer')

// 用户管理
const users = ref([])
const userDialogVisible = ref(false)
const currentUser = ref({})

// 歌手管理
const singers = ref([])
const singerDialogVisible = ref(false)
const singerDialogTitle = ref('编辑歌手')
const currentSinger = ref({})

// 音乐管理
const songs = ref([])
const songDialogVisible = ref(false)
const songDialogTitle = ref('编辑音乐')
const currentSong = ref({})
const songFileList = ref([])

// 歌单管理
const songLists = ref([])
const songListDialogVisible = ref(false)
const songListDialogTitle = ref('编辑歌单')
const currentSongList = ref({})
const songListSongsDialogVisible = ref(false)
const currentSongListSongs = ref([])
const selectedSongId = ref(null)

onMounted(() => {
  loadUsers()
})

// 菜单选择
const handleMenuSelect = (key) => {
  activeMenu.value = key
  if (key === 'consumer') {
    loadUsers()
  } else if (key === 'singer') {
    loadSingers()
  } else if (key === 'song') {
    loadSongs()
    loadSingers()
  } else if (key === 'songlist') {
    loadSongLists()
  }
}

// 退出登录
const logout = () => {
  localStorage.removeItem('username')
  localStorage.removeItem('userId')
  localStorage.removeItem('isAdmin')
  router.push('/login')
}

// 获取文件URL
const getFileUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `http://localhost:8082${url}`
}

// ========== 用户管理 ==========
const loadUsers = async () => {
  try {
    const res = await axios.get(`${API_BASE}/users`)
    users.value = res.data
  } catch (error) {
    ElMessage.error('加载用户数据失败')
  }
}

const editUser = (user) => {
  currentUser.value = { ...user }
  userDialogVisible.value = true
}

const saveUser = async () => {
  try {
    await axios.put(`${API_BASE}/users/${currentUser.value.id}`, currentUser.value)
    ElMessage.success('保存成功')
    userDialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteUser = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个用户吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`${API_BASE}/users/${id}`)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleAvatarSuccess = (res) => {
  if (res.success) {
    currentUser.value.avatarUrl = res.url
    ElMessage.success('上传成功')
  }
}

// ========== 歌手管理 ==========
const loadSingers = async () => {
  try {
    const res = await axios.get(`${API_BASE}/music/singer`)
    singers.value = res.data
  } catch (error) {
    ElMessage.error('加载歌手数据失败')
  }
}

const addSinger = () => {
  currentSinger.value = { name: '', picUrl: '', introduction: '' }
  singerDialogTitle.value = '添加歌手'
  singerDialogVisible.value = true
}

const editSinger = (singer) => {
  currentSinger.value = { ...singer }
  singerDialogTitle.value = '编辑歌手'
  singerDialogVisible.value = true
}

const saveSinger = async () => {
  try {
    if (currentSinger.value.id) {
      await axios.post(`${API_BASE}/music/singer/update`, currentSinger.value)
    } else {
      await axios.post(`${API_BASE}/music/singer/add`, currentSinger.value)
    }
    ElMessage.success('保存成功')
    singerDialogVisible.value = false
    loadSingers()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteSinger = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个歌手吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`${API_BASE}/music/singer/delete?id=${id}`)
    ElMessage.success('删除成功')
    loadSingers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSingerPicSuccess = (res) => {
  if (res.success) {
    currentSinger.value.picUrl = res.url
    ElMessage.success('上传成功')
  }
}

// ========== 音乐管理 ==========
const loadSongs = async () => {
  try {
    const res = await axios.get(`${API_BASE}/music/song`)
    songs.value = res.data
  } catch (error) {
    ElMessage.error('加载音乐数据失败')
  }
}

const addSong = () => {
  currentSong.value = {
    name: '',
    singerId: null,
    singerName: '',
    songUrl: '',
    picUrl: '',
    lyric: '',
    duration: 0
  }
  songFileList.value = []
  songDialogTitle.value = '添加音乐'
  songDialogVisible.value = true
}

const editSong = (song) => {
  currentSong.value = { ...song }
  songFileList.value = []
  songDialogTitle.value = '编辑音乐'
  songDialogVisible.value = true
}

const saveSong = async () => {
  try {
    if (!currentSong.value.songUrl) {
      ElMessage.error('请上传音乐文件')
      return
    }
    if (currentSong.value.id) {
      await axios.post(`${API_BASE}/music/song/update`, currentSong.value)
    } else {
      await axios.post(`${API_BASE}/music/song/add`, currentSong.value)
    }
    ElMessage.success('保存成功')
    songDialogVisible.value = false
    loadSongs()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteSong = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这首歌曲吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`${API_BASE}/music/song/delete?id=${id}`)
    ElMessage.success('删除成功')
    loadSongs()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSongPicSuccess = (res) => {
  if (res.success) {
    currentSong.value.picUrl = res.url
    ElMessage.success('封面上传成功')
  }
}

const handleSongFileSuccess = (res) => {
  if (res.success) {
    currentSong.value.songUrl = res.url
    ElMessage.success('音乐文件上传成功')
  }
}

const handleSingerChange = (singerId) => {
  const singer = singers.value.find(s => s.id === singerId)
  if (singer) {
    currentSong.value.singerName = singer.name
  }
}

// ========== 歌单管理 ==========
const loadSongLists = async () => {
  try {
    const res = await axios.get(`${API_BASE}/music/songlist/all`)
    songLists.value = res.data
  } catch (error) {
    ElMessage.error('加载歌单数据失败')
  }
}

const addSongList = () => {
  currentSongList.value = { title: '', picUrl: '', introduction: '', style: '' }
  songListDialogTitle.value = '添加歌单'
  songListDialogVisible.value = true
}

const editSongList = (songList) => {
  currentSongList.value = { ...songList }
  songListDialogTitle.value = '编辑歌单'
  songListDialogVisible.value = true
}

const saveSongList = async () => {
  try {
    if (currentSongList.value.id) {
      await axios.post(`${API_BASE}/music/songlist/update`, currentSongList.value)
    } else {
      await axios.post(`${API_BASE}/music/songlist/add`, currentSongList.value)
    }
    ElMessage.success('保存成功')
    songListDialogVisible.value = false
    loadSongLists()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteSongList = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个歌单吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`${API_BASE}/music/songlist/delete?id=${id}`)
    ElMessage.success('删除成功')
    loadSongLists()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSongListPicSuccess = (res) => {
  if (res.success) {
    currentSongList.value.picUrl = res.url
    ElMessage.success('上传成功')
  }
}

// 管理歌单中的歌曲
const manageSongs = async (songList) => {
  currentSongList.value = { ...songList }
  selectedSongId.value = null
  try {
    // 加载歌单中的歌曲
    const res = await axios.get(`${API_BASE}/music/songlist/songs?id=${songList.id}`)
    currentSongListSongs.value = res.data

    // 确保加载所有歌曲列表
    if (songs.value.length === 0) {
      await loadSongs()
    }

    songListSongsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载歌单歌曲失败')
  }
}

const addSongToCurrentList = async () => {
  if (!selectedSongId.value) {
    ElMessage.warning('请选择要添加的歌曲')
    return
  }

  try {
    await axios.post(`${API_BASE}/music/songlist/add-song`, {
      songListId: currentSongList.value.id,
      songId: selectedSongId.value
    })
    ElMessage.success('添加成功')
    selectedSongId.value = null

    // 重新加载歌单中的歌曲
    const res = await axios.get(`${API_BASE}/music/songlist/songs?id=${currentSongList.value.id}`)
    currentSongListSongs.value = res.data
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const removeSongFromCurrentList = async (songId) => {
  try {
    await ElMessageBox.confirm('确定要从歌单中移除这首歌曲吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`${API_BASE}/music/songlist/remove-song?songListId=${currentSongList.value.id}&songId=${songId}`)
    ElMessage.success('移除成功')

    // 重新加载歌单中的歌曲
    const res = await axios.get(`${API_BASE}/music/songlist/songs?id=${currentSongList.value.id}`)
    currentSongListSongs.value = res.data
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('移除失败')
    }
  }
}
</script>

<style scoped>
.admin-container {
  height: 100vh;
  background-color: #121212;
}

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

.aside {
  background-color: #000000;
  color: #fff;
  height: 100%;
}

.main {
  background-color: #121212;
  padding: 24px;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  color: #FFFFFF;
}

.main h3 {
  color: #FFFFFF;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 700;
}

.avatar-img {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-uploader .avatar {
  width: 150px;
  height: 150px;
  display: block;
  border-radius: 8px;
  object-fit: cover;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
}

.avatar-uploader-icon:hover {
  border-color: #1db954;
}

/* 表格样式 */
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

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #181818;
}

/* 对话框样式 */
:deep(.el-dialog) {
  background-color: #181818;
}

:deep(.el-dialog__title) {
  color: #FFFFFF;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #FFFFFF;
}

:deep(.el-form-item__label) {
  color: #FFFFFF;
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

:deep(.el-textarea__inner) {
  background-color: #282828;
  color: #FFFFFF;
  box-shadow: none;
}

:deep(.el-select .el-input__wrapper) {
  background-color: #282828;
}

:deep(.el-select-dropdown) {
  background-color: #282828;
}

:deep(.el-select-dropdown__item) {
  color: #FFFFFF;
}

:deep(.el-select-dropdown__item:hover) {
  background-color: #383838;
}

:deep(.el-select-dropdown__item.selected) {
  color: #1db954;
}

:deep(.el-divider) {
  border-color: #282828;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-upload) {
  border: 1px dashed #282828;
  border-radius: 8px;
  background-color: #282828;
}

:deep(.el-upload:hover) {
  border-color: #1db954;
}
</style>
