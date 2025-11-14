package com.example.stusyshomework.repository;

import com.example.stusyshomework.entity.CardRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface CardRecordRepository extends JpaRepository<CardRecord, Integer> {

    List<CardRecord> findByCardNoOrderByCreatedAtDesc(String cardNo);

    List<CardRecord> findByTypeOrderByCreatedAtDesc(String type);

    @Query("SELECT SUM(r.amount) FROM CardRecord r WHERE r.type = ?1")
    BigDecimal sumAmountByType(String type);

    @Query("SELECT SUM(r.amount) FROM CardRecord r WHERE r.type = ?1 AND DATE(r.createdAt) = DATE(?2)")
    BigDecimal sumAmountByTypeAndDate(String type, Date date);

    List<CardRecord> findByCard_IdOrderByCreatedAtDesc(Integer cardId);

    @Query("SELECT r FROM CardRecord r WHERE r.cardNo LIKE %?1% OR r.type LIKE %?1%")
    List<CardRecord> searchRecords(String keyword);
}
