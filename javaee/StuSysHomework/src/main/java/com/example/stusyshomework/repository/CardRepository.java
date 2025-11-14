package com.example.stusyshomework.repository;

import com.example.stusyshomework.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    Optional<Card> findByCardNo(String cardNo);

    List<Card> findByStatus(String status);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.status = ?1")
    Long countByStatus(String status);

    @Query("SELECT c FROM Card c WHERE c.cardNo LIKE %?1% OR c.status LIKE %?1%")
    List<Card> searchCards(String keyword);

    Optional<Card> findByStudent_Id(Integer studentId);
}
