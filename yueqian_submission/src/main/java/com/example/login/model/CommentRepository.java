package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findBySongIdOrderByCreateTimeDesc(Integer songId);

    List<Comment> findByUserId(Integer userId);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.songId = ?1")
    Double getAverageRatingBySongId(Integer songId);

    Long countBySongId(Integer songId);
}
