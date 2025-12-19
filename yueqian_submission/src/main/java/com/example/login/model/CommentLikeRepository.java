package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {

    boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);

    Long countByCommentId(Integer commentId);

    @Transactional
    void deleteByUserIdAndCommentId(Integer userId, Integer commentId);

    List<CommentLike> findByUserId(Integer userId);
}
