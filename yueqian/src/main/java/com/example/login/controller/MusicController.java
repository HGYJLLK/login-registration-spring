package com.example.login.controller;

import com.example.login.model.Singer;
import com.example.login.model.Song;
import com.example.login.model.SongList;
import com.example.login.model.ListSong;
import com.example.login.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    // ==================== Singer API ====================

    /**
     * 获取所有歌手
     * GET /music/singer
     */
    @GetMapping("/singer")
    public ResponseEntity<?> getAllSingers() {
        List<Singer> singers = musicService.getAllSingers();
        return ResponseEntity.ok(singers);
    }

    /**
     * 根据性别获取歌手（示例API，当前未实现性别字段）
     * GET /music/singer/sex/detail?sex=男
     */
    @GetMapping("/singer/sex/detail")
    public ResponseEntity<?> getSingersBySex(@RequestParam String sex) {
        // 目前返回所有歌手，如果需要性别分类，需要在数据库中添加sex字段
        List<Singer> singers = musicService.getAllSingers();
        return ResponseEntity.ok(singers);
    }

    /**
     * 根据歌手名搜索
     * GET /music/singer/name/detail?name=周杰伦
     */
    @GetMapping("/singer/name/detail")
    public ResponseEntity<?> searchSingersByName(@RequestParam String name) {
        List<Singer> singers = musicService.searchSingersByName(name);
        return ResponseEntity.ok(singers);
    }

    // ==================== Song API ====================

    /**
     * 根据ID获取歌曲详情
     * GET /music/song/detail?id=1
     */
    @GetMapping("/song/detail")
    public ResponseEntity<?> getSongDetail(@RequestParam Integer id) {
        Optional<Song> song = musicService.getSongById(id);
        if (song.isPresent()) {
            return ResponseEntity.ok(song.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "歌曲不存在");
            response.put("success", false);
            return ResponseEntity.status(404).body(response);
        }
    }

    /**
     * 根据歌手ID获取歌曲列表
     * GET /music/song/singer/detail?singerId=1
     */
    @GetMapping("/song/singer/detail")
    public ResponseEntity<?> getSongsBySingerId(@RequestParam Integer singerId) {
        List<Song> songs = musicService.getSongsBySingerId(singerId);
        return ResponseEntity.ok(songs);
    }

    /**
     * 根据歌手名或歌名搜索歌曲
     * GET /music/song/singerName/detail?name=周杰伦
     */
    @GetMapping("/song/singerName/detail")
    public ResponseEntity<?> searchSongsBySingerName(@RequestParam String name) {
        List<Song> songs = musicService.searchSongs(name);
        return ResponseEntity.ok(songs);
    }

    /**
     * 获取所有歌曲
     * GET /music/song
     */
    @GetMapping("/song")
    public ResponseEntity<?> getAllSongs() {
        List<Song> songs = musicService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    // ==================== 歌单相关API（示例，可扩展）====================

    /**
     * 获取所有"歌单"（目前返回按歌手分组的歌曲列表）
     * GET /music/songList
     */
    @GetMapping("/songList")
    public ResponseEntity<?> getSongList() {
        List<Singer> singers = musicService.getAllSingers();
        return ResponseEntity.ok(singers);
    }

    /**
     * 获取指定歌单的歌曲
     * GET /music/listSong/detail?songListId=1
     */
    @GetMapping("/listSong/detail")
    public ResponseEntity<?> getListSongs(@RequestParam Integer songListId) {
        // songListId 对应歌手ID
        List<Song> songs = musicService.getSongsBySingerId(songListId);
        return ResponseEntity.ok(songs);
    }

    /**
     * 根据歌单类型获取歌单
     * GET /music/songList/style/detail?style=华语
     */
    @GetMapping("/songList/style/detail")
    public ResponseEntity<?> getSongListByStyle(@RequestParam String style) {
        // 示例：返回所有歌手
        List<Singer> singers = musicService.getAllSingers();
        return ResponseEntity.ok(singers);
    }

    /**
     * 根据标题搜索歌单
     * GET /music/songList/likeTitle/detail?title=周杰伦
     */
    @GetMapping("/songList/likeTitle/detail")
    public ResponseEntity<?> searchSongListByTitle(@RequestParam String title) {
        List<Singer> singers = musicService.searchSingersByName(title);
        return ResponseEntity.ok(singers);
    }

    // ==================== 管理API（添加、更新、删除）====================

    /**
     * 添加歌手
     * POST /music/singer/add
     */
    @PostMapping("/singer/add")
    public ResponseEntity<?> addSinger(@RequestBody Singer singer) {
        Singer savedSinger = musicService.addSinger(singer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "添加成功");
        response.put("success", true);
        response.put("data", savedSinger);
        return ResponseEntity.ok(response);
    }

    /**
     * 添加歌曲
     * POST /music/song/add
     */
    @PostMapping("/song/add")
    public ResponseEntity<?> addSong(@RequestBody Song song) {
        Song savedSong = musicService.addSong(song);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "添加成功");
        response.put("success", true);
        response.put("data", savedSong);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新歌手信息
     * POST /music/singer/update
     */
    @PostMapping("/singer/update")
    public ResponseEntity<?> updateSinger(@RequestBody Singer singer) {
        Singer updatedSinger = musicService.updateSinger(singer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "更新成功");
        response.put("success", true);
        response.put("data", updatedSinger);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新歌曲信息
     * POST /music/song/update
     */
    @PostMapping("/song/update")
    public ResponseEntity<?> updateSong(@RequestBody Song song) {
        Song updatedSong = musicService.updateSong(song);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "更新成功");
        response.put("success", true);
        response.put("data", updatedSong);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除歌手
     * DELETE /music/singer/delete?id=1
     */
    @DeleteMapping("/singer/delete")
    public ResponseEntity<?> deleteSinger(@RequestParam Integer id) {
        musicService.deleteSinger(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "删除成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除歌曲
     * DELETE /music/song/delete?id=1
     */
    @DeleteMapping("/song/delete")
    public ResponseEntity<?> deleteSong(@RequestParam Integer id) {
        musicService.deleteSong(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "删除成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // ==================== 歌单管理API ====================

    /**
     * 获取所有歌单
     * GET /music/songlist/all
     */
    @GetMapping("/songlist/all")
    public ResponseEntity<?> getAllSongLists() {
        List<SongList> songLists = musicService.getAllSongLists();
        return ResponseEntity.ok(songLists);
    }

    /**
     * 添加歌单
     * POST /music/songlist/add
     */
    @PostMapping("/songlist/add")
    public ResponseEntity<?> addSongList(@RequestBody SongList songList) {
        SongList saved = musicService.addSongList(songList);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "添加成功");
        response.put("success", true);
        response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新歌单
     * POST /music/songlist/update
     */
    @PostMapping("/songlist/update")
    public ResponseEntity<?> updateSongList(@RequestBody SongList songList) {
        SongList updated = musicService.updateSongList(songList);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "更新成功");
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除歌单
     * DELETE /music/songlist/delete?id=1
     */
    @DeleteMapping("/songlist/delete")
    public ResponseEntity<?> deleteSongList(@RequestParam Integer id) {
        musicService.deleteSongList(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "删除成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取歌单中的歌曲列表
     * GET /music/songlist/songs?id=1
     */
    @GetMapping("/songlist/songs")
    public ResponseEntity<?> getSongListSongs(@RequestParam Integer id) {
        List<Song> songs = musicService.getSongListSongs(id);
        return ResponseEntity.ok(songs);
    }

    /**
     * 添加歌曲到歌单
     * POST /music/songlist/add-song
     */
    @PostMapping("/songlist/add-song")
    public ResponseEntity<?> addSongToList(@RequestBody Map<String, Integer> params) {
        Integer songListId = params.get("songListId");
        Integer songId = params.get("songId");
        musicService.addSongToList(songListId, songId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "添加成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 从歌单移除歌曲
     * DELETE /music/songlist/remove-song
     */
    @DeleteMapping("/songlist/remove-song")
    public ResponseEntity<?> removeSongFromList(@RequestParam Integer songListId, @RequestParam Integer songId) {
        musicService.removeSongFromList(songListId, songId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "移除成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
