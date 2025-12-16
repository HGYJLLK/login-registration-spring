import request from './request'

// 获取所有歌手
export const getAllSingers = () => {
  return request.get('/music/singer')
}

// 根据歌手名搜索
export const searchSingersByName = (name) => {
  return request.get('/music/singer/name/detail', {
    params: { name }
  })
}

// 获取所有歌曲
export const getAllSongs = () => {
  return request.get('/music/song')
}

// 根据ID获取歌曲详情
export const getSongById = (id) => {
  return request.get('/music/song/detail', {
    params: { id }
  })
}

// 根据歌手ID获取歌曲列表
export const getSongsBySingerId = (singerId) => {
  return request.get('/music/song/singer/detail', {
    params: { singerId }
  })
}

// 搜索歌曲（根据歌名或歌手名）
export const searchSongs = (name) => {
  return request.get('/music/song/singerName/detail', {
    params: { name }
  })
}

// 获取歌单列表
export const getSongList = () => {
  return request.get('/music/songList')
}

// 获取指定歌单的歌曲
export const getListSongs = (songListId) => {
  return request.get('/music/listSong/detail', {
    params: { songListId }
  })
}
