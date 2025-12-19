package com.example.login.controller;

import com.example.login.model.Singer;
import com.example.login.model.Song;
import com.example.login.model.SongList;
import com.example.login.model.ListSong;
import com.example.login.model.Comment;
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

    // ==================== 收藏管理API ====================

    /**
     * 添加收藏
     * POST /music/favorite/add
     */
    @PostMapping("/favorite/add")
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer songId = Integer.valueOf(params.get("songId").toString());
        musicService.addFavorite(userId, songId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "收藏成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 取消收藏
     * DELETE /music/favorite/remove
     */
    @DeleteMapping("/favorite/remove")
    public ResponseEntity<?> removeFavorite(@RequestParam Long userId, @RequestParam Integer songId) {
        musicService.removeFavorite(userId, songId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "已取消收藏");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户收藏的歌曲
     * GET /music/favorite/list?userId=1
     */
    @GetMapping("/favorite/list")
    public ResponseEntity<?> getFavorites(@RequestParam Long userId) {
        List<Song> songs = musicService.getUserFavorites(userId);
        return ResponseEntity.ok(songs);
    }

    /**
     * 检查是否已收藏
     * GET /music/favorite/check?userId=1&songId=1
     */
    @GetMapping("/favorite/check")
    public ResponseEntity<?> checkFavorite(@RequestParam Long userId, @RequestParam Integer songId) {
        boolean isFavorite = musicService.isFavorite(userId, songId);
        Map<String, Object> response = new HashMap<>();
        response.put("isFavorite", isFavorite);
        return ResponseEntity.ok(response);
    }

    // ==================== 歌曲风格API ====================

    /**
     * 根据风格搜索歌曲
     * GET /api/music/song/style?style=流行
     */
    @GetMapping("/song/style")
    public ResponseEntity<?> searchSongsByStyle(@RequestParam String style) {
        List<Song> songs = musicService.searchSongsByStyle(style);
        for (Song song : songs) {
            song.setAverageRating(musicService.getSongAverageRating(song.getId()));
            song.setCommentCount(musicService.getSongCommentCount(song.getId()));
            song.setTotalLikes(musicService.getSongTotalLikes(song.getId()));
        }
        return ResponseEntity.ok(songs);
    }

    /**
     * 获取所有歌曲风格
     * GET /api/music/song/styles
     */
    @GetMapping("/song/styles")
    public ResponseEntity<?> getAllStyles() {
        List<String> styles = musicService.getAllStyles();
        return ResponseEntity.ok(styles);
    }

    /**
     * 获取歌曲详情（含统计信息）
     * GET /api/music/song/detail-with-stats?id=1
     */
    @GetMapping("/song/detail-with-stats")
    public ResponseEntity<?> getSongDetailWithStats(@RequestParam Integer id) {
        Optional<Song> song = musicService.getSongDetailWithStats(id);
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
     * 获取所有歌曲（含统计信息）
     * GET /api/music/song/all-with-stats
     */
    @GetMapping("/song/all-with-stats")
    public ResponseEntity<?> getAllSongsWithStats() {
        List<Song> songs = musicService.getAllSongsWithStats();
        return ResponseEntity.ok(songs);
    }

    // ==================== 评论API ====================

    /**
     * 添加评论（含评分）
     * POST /api/music/comment/add
     * Body: { "userId": 1, "songId": 1, "content": "很好听", "rating": 5 }
     */
    @PostMapping("/comment/add")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        try {
            Comment saved = musicService.addComment(comment);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "评论成功");
            response.put("success", true);
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取歌曲的评论列表
     * GET /api/music/comment/list?songId=1&userId=1
     */
    @GetMapping("/comment/list")
    public ResponseEntity<?> getSongComments(
            @RequestParam Integer songId,
            @RequestParam(required = false) Integer userId) {
        List<Comment> comments = musicService.getSongComments(songId, userId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 删除评论
     * DELETE /api/music/comment/delete?id=1
     */
    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteComment(@RequestParam Integer id) {
        musicService.deleteComment(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "删除成功");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取歌曲平均评分
     * GET /api/music/song/rating?songId=1
     */
    @GetMapping("/song/rating")
    public ResponseEntity<?> getSongRating(@RequestParam Integer songId) {
        Double rating = musicService.getSongAverageRating(songId);
        Map<String, Object> response = new HashMap<>();
        response.put("rating", rating);
        response.put("commentCount", musicService.getSongCommentCount(songId));
        return ResponseEntity.ok(response);
    }

    // ==================== 点赞API ====================

    /**
     * 点赞歌曲（累计）
     * POST /api/music/song/like
     * Body: { "userId": 1, "songId": 1 }
     */
    @PostMapping("/song/like")
    public ResponseEntity<?> likeSong(@RequestBody Map<String, Object> params) {
        Integer userId = Integer.valueOf(params.get("userId").toString());
        Integer songId = Integer.valueOf(params.get("songId").toString());
        Integer likeCount = musicService.likeSong(userId, songId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "点赞成功");
        response.put("success", true);
        response.put("likeCount", likeCount);
        response.put("totalLikes", musicService.getSongTotalLikes(songId));
        return ResponseEntity.ok(response);
    }

    /**
     * 获取歌曲点赞数
     * GET /api/music/song/likes?songId=1
     */
    @GetMapping("/song/likes")
    public ResponseEntity<?> getSongLikes(@RequestParam Integer songId) {
        Integer totalLikes = musicService.getSongTotalLikes(songId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalLikes", totalLikes);
        return ResponseEntity.ok(response);
    }

    /**
     * 点赞/取消点赞评论
     * POST /api/music/comment/like
     * Body: { "userId": 1, "commentId": 1 }
     */
    @PostMapping("/comment/like")
    public ResponseEntity<?> toggleCommentLike(@RequestBody Map<String, Object> params) {
        Integer userId = Integer.valueOf(params.get("userId").toString());
        Integer commentId = Integer.valueOf(params.get("commentId").toString());
        boolean isLiked = musicService.toggleCommentLike(userId, commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", isLiked ? "点赞成功" : "已取消点赞");
        response.put("success", true);
        response.put("isLiked", isLiked);
        response.put("likeCount", musicService.getCommentLikeCount(commentId));
        return ResponseEntity.ok(response);
    }
}
