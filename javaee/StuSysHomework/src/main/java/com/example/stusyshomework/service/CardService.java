package com.example.stusyshomework.service;

import com.example.stusyshomework.entity.Card;
import com.example.stusyshomework.entity.CardRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CardService {

    List<Card> getAllCards();

    Card getCardById(Integer id);

    Card getCardByCardNo(String cardNo);

    Card saveCard(Card card);

    void deleteCard(Integer id);

    List<Card> searchCards(String keyword);

    List<Card> getCardsByStatus(String status);

    Card bindStudent(Integer cardId, Integer studentId);

    Card unbindStudent(Integer cardId);

    Card recharge(Integer cardId, BigDecimal amount, String remark);

    Card consume(Integer cardId, BigDecimal amount, String remark);

    List<CardRecord> getCardRecords(Integer cardId);

    List<CardRecord> getAllRecords();

    Map<String, Object> getCardStatistics();

    Map<String, Object> getFinancialStatistics();

    Card getCardByStudentId(Integer studentId);
}
