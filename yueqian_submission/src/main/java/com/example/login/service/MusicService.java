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
import com.example.login.model.Comment;
import com.example.login.model.CommentRepository;
import com.example.login.model.SongLike;
import com.example.login.model.SongLikeRepository;
import com.example.login.model.CommentLike;
import com.example.login.model.CommentLikeRepository;
import com.example.login.model.User;
import com.example.login.model.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SongLikeRepository songLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private UserRepository userRepository;

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

    // ==================== 歌曲风格相关 ====================

    // 根据风格搜索歌曲
    public List<Song> searchSongsByStyle(String style) {
        return songRepository.findByStyleContaining(style);
    }

    // 获取所有歌曲风格
    public List<String> getAllStyles() {
        return songRepository.findDistinctStyles();
    }

    // ==================== 评论相关 ====================

    // 添加评论（含评分）
    public Comment addComment(Comment comment) {
        if (comment.getRating() < 1 || comment.getRating() > 5) {
            throw new IllegalArgumentException("评分必须在1-5之间");
        }
        return commentRepository.save(comment);
    }

    // 获取歌曲的所有评论（含用户信息和点赞信息）
    public List<Comment> getSongComments(Integer songId, Integer currentUserId) {
        List<Comment> comments = commentRepository.findBySongIdOrderByCreateTimeDesc(songId);

        for (Comment comment : comments) {
            Optional<User> user = userRepository.findById(Long.valueOf(comment.getUserId()));
            if (user.isPresent()) {
                comment.setUsername(user.get().getUsername());
                comment.setAvatarUrl(user.get().getAvatarUrl());
            }

            Long likeCount = commentLikeRepository.countByCommentId(comment.getId());
            comment.setLikeCount(likeCount.intValue());

            if (currentUserId != null) {
                boolean isLiked = commentLikeRepository.existsByUserIdAndCommentId(currentUserId, comment.getId());
                comment.setIsLiked(isLiked);
            }
        }

        return comments;
    }

    // 删除评论
    @Transactional
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    // 获取歌曲平均评分
    public Double getSongAverageRating(Integer songId) {
        Double avg = commentRepository.getAverageRatingBySongId(songId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    // 获取歌曲评论数
    public Long getSongCommentCount(Integer songId) {
        return commentRepository.countBySongId(songId);
    }

    // ==================== 歌曲点赞相关 ====================

    // 点赞歌曲（累计）
    @Transactional
    public Integer likeSong(Integer userId, Integer songId) {
        Optional<SongLike> existing = songLikeRepository.findByUserIdAndSongId(userId, songId);

        if (existing.isPresent()) {
            SongLike songLike = existing.get();
            songLike.setLikeCount(songLike.getLikeCount() + 1);
            songLike.setUpdateTime(LocalDateTime.now());
            songLikeRepository.save(songLike);
            return songLike.getLikeCount();
        } else {
            SongLike songLike = new SongLike(userId, songId);
            songLikeRepository.save(songLike);
            return 1;
        }
    }

    // 获取歌曲总点赞数
    public Integer getSongTotalLikes(Integer songId) {
        return songLikeRepository.getTotalLikesBySongId(songId);
    }

    // ==================== 评论点赞相关 ====================

    // 点赞/取消点赞评论（toggle）
    @Transactional
    public boolean toggleCommentLike(Integer userId, Integer commentId) {
        if (commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
            commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);
            return false;
        } else {
            CommentLike commentLike = new CommentLike(userId, commentId);
            commentLikeRepository.save(commentLike);
            return true;
        }
    }

    // 获取评论点赞数
    public Long getCommentLikeCount(Integer commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    // ==================== 增强版获取歌曲详情 ====================

    // 获取歌曲详情（含评分、点赞等统计信息）
    public Optional<Song> getSongDetailWithStats(Integer songId) {
        Optional<Song> songOpt = songRepository.findById(songId);
        if (songOpt.isPresent()) {
            Song song = songOpt.get();
            song.setAverageRating(getSongAverageRating(songId));
            song.setCommentCount(getSongCommentCount(songId));
            song.setTotalLikes(getSongTotalLikes(songId));
            return Optional.of(song);
        }
        return Optional.empty();
    }

    // 获取所有歌曲（含统计信息）
    public List<Song> getAllSongsWithStats() {
        List<Song> songs = songRepository.findAll();
        for (Song song : songs) {
            song.setAverageRating(getSongAverageRating(song.getId()));
            song.setCommentCount(getSongCommentCount(song.getId()));
            song.setTotalLikes(getSongTotalLikes(song.getId()));
        }
        return songs;
    }
}
