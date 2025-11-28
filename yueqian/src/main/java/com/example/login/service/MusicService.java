package com.example.login.service;

import com.example.login.model.Singer;
import com.example.login.model.SingerRepository;
import com.example.login.model.Song;
import com.example.login.model.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicService {

    @Autowired
    private SingerRepository singerRepository;

    @Autowired
    private SongRepository songRepository;

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
}
