package com.example.login.service;

import com.example.login.model.Singer;
import com.example.login.model.SingerRepository;
import com.example.login.model.Song;
import com.example.login.model.SongRepository;
import com.example.login.model.SongList;
import com.example.login.model.SongListRepository;
import com.example.login.model.ListSong;
import com.example.login.model.ListSongRepository;
import com.example.login.model.Favorite;
import com.example.login.model.FavoriteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicService {

    @Autowired
    private SingerRepository singerRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongListRepository songListRepository;

    @Autowired
    private ListSongRepository listSongRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    // ==================== Singer Methods ====================

    // 获取所有歌手
    public List<Singer> getAllSingers() {
        return singerRepository.findAll();
    }

    // 根据ID获取歌手
    public Optional<Singer> getSingerById(Integer id) {
        return singerRepository.findById(id);
    }

    // 根据名称搜索歌手
    public List<Singer> searchSingersByName(String name) {
        return singerRepository.findByNameContaining(name);
    }

    // 添加歌手
    public Singer addSinger(Singer singer) {
        return singerRepository.save(singer);
    }

    // 更新歌手
    public Singer updateSinger(Singer singer) {
        return singerRepository.save(singer);
    }

    // 删除歌手
    public void deleteSinger(Integer id) {
        singerRepository.deleteById(id);
    }

    // ==================== Song Methods ====================

    // 获取所有歌曲
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // 根据ID获取歌曲
    public Optional<Song> getSongById(Integer id) {
        return songRepository.findById(id);
    }

    // 根据歌手ID获取歌曲列表
    public List<Song> getSongsBySingerId(Integer singerId) {
        return songRepository.findBySingerId(singerId);
    }

    // 根据歌名搜索歌曲
    public List<Song> searchSongsByName(String name) {
        return songRepository.findByNameContaining(name);
    }

    // 根据歌手名搜索歌曲
    public List<Song> searchSongsBySingerName(String singerName) {
        return songRepository.findBySingerNameContaining(singerName);
    }

    // 根据歌名或歌手名搜索
    public List<Song> searchSongs(String keywords) {
        return songRepository.findByNameContainingOrSingerNameContaining(keywords, keywords);
    }

    // 添加歌曲
    public Song addSong(Song song) {
        return songRepository.save(song);
    }

    // 更新歌曲
    public Song updateSong(Song song) {
        return songRepository.save(song);
    }

    // 删除歌曲
    public void deleteSong(Integer id) {
        songRepository.deleteById(id);
    }

    // ==================== SongList Methods ====================

    // 获取所有歌单
    public List<SongList> getAllSongLists() {
        return songListRepository.findAll();
    }

    // 添加歌单
    public SongList addSongList(SongList songList) {
        return songListRepository.save(songList);
    }

    // 更新歌单
    public SongList updateSongList(SongList songList) {
        return songListRepository.save(songList);
    }

    // 删除歌单
    @Transactional
    public void deleteSongList(Integer id) {
        listSongRepository.deleteBySongListId(id);
        songListRepository.deleteById(id);
    }

    // 获取歌单中的歌曲列表
    public List<Song> getSongListSongs(Integer songListId) {
        List<ListSong> listSongs = listSongRepository.findBySongListId(songListId);
        List<Integer> songIds = listSongs.stream()
                .map(ListSong::getSongId)
                .collect(Collectors.toList());
        return songRepository.findAllById(songIds);
    }

    // 添加歌曲到歌单
    public void addSongToList(Integer songListId, Integer songId) {
        ListSong listSong = new ListSong(songListId, songId);
        listSongRepository.save(listSong);
    }

    // 从歌单移除歌曲
    @Transactional
    public void removeSongFromList(Integer songListId, Integer songId) {
        listSongRepository.deleteBySongListIdAndSongId(songListId, songId);
    }

    // ==================== Favorite Methods ====================

    // 添加收藏
    public void addFavorite(Long userId, Integer songId) {
        if (!favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            Favorite favorite = new Favorite(userId, songId);
            favoriteRepository.save(favorite);
        }
    }

    // 取消收藏
    @Transactional
    public void removeFavorite(Long userId, Integer songId) {
        favoriteRepository.deleteByUserIdAndSongId(userId, songId);
    }

    // 获取用户收藏的歌曲列表
    public List<Song> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        List<Integer> songIds = favorites.stream()
                .map(Favorite::getSongId)
                .collect(Collectors.toList());
        return songRepository.findAllById(songIds);
    }

    // 检查是否已收藏
    public boolean isFavorite(Long userId, Integer songId) {
        return favoriteRepository.existsByUserIdAndSongId(userId, songId);
    }
}
