package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

    // 根据歌手ID查找歌曲
    List<Song> findBySingerId(Integer singerId);

    // 根据歌名查找（模糊搜索）
    List<Song> findByNameContaining(String name);

    // 根据歌手名查找（模糊搜索）
    List<Song> findBySingerNameContaining(String singerName);

    // 根据歌名和歌手名查找
    List<Song> findByNameContainingOrSingerNameContaining(String name, String singerName);

    // 根据风格查找（模糊搜索）
    List<Song> findByStyleContaining(String style);

    // 获取所有不同的风格
    @Query("SELECT DISTINCT s.style FROM Song s WHERE s.style IS NOT NULL")
    List<String> findDistinctStyles();
}
