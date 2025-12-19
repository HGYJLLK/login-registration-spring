package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongLikeRepository extends JpaRepository<SongLike, Integer> {

    Optional<SongLike> findByUserIdAndSongId(Integer userId, Integer songId);

    @Query("SELECT COALESCE(SUM(sl.likeCount), 0) FROM SongLike sl WHERE sl.songId = ?1")
    Integer getTotalLikesBySongId(Integer songId);

    boolean existsByUserIdAndSongId(Integer userId, Integer songId);
}
