package com.example.stusyshomework.service.impl;

import com.example.stusyshomework.entity.Card;
import com.example.stusyshomework.entity.CardRecord;
import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.repository.CardRecordRepository;
import com.example.stusyshomework.repository.CardRepository;
import com.example.stusyshomework.repository.StudentRepository;
import com.example.stusyshomework.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardRecordRepository cardRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card getCardById(Integer id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card getCardByCardNo(String cardNo) {
        return cardRepository.findByCardNo(cardNo).orElse(null);
    }

    @Override
    @Transactional
    public Card saveCard(Card card) {
        card.setUpdatedAt(new Date());
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public void deleteCard(Integer id) {
        cardRepository.deleteById(id);
    }

    @Override
    public List<Card> searchCards(String keyword) {
        return cardRepository.searchCards(keyword);
    }

    @Override
    public List<Card> getCardsByStatus(String status) {
        return cardRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Card bindStudent(Integer cardId, Integer studentId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);

        if (card == null || student == null) {
            return null;
        }

        if (!"未启用".equals(card.getStatus())) {
            throw new RuntimeException("只有未启用的卡才能绑定学生");
        }

        card.setStudent(student);
        card.setStatus("使用中");
        card.setUpdatedAt(new Date());

        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card unbindStudent(Integer cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            return null;
        }

        card.setStudent(null);
        card.setStatus("未启用");
        card.setUpdatedAt(new Date());

        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card recharge(Integer cardId, BigDecimal amount, String remark) {
        Card card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            return null;
        }

        BigDecimal oldBalance = card.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);
        card.setBalance(newBalance);
        card.setUpdatedAt(new Date());

        Card savedCard = cardRepository.save(card);

        CardRecord record = new CardRecord();
        record.setCard(card);
        record.setCardNo(card.getCardNo());
        record.setType("充值");
        record.setAmount(amount);
        record.setBalanceAfter(newBalance);
        record.setRemark(remark);
        cardRecordRepository.save(record);

        return savedCard;
    }

    @Override
    @Transactional
    public Card consume(Integer cardId, BigDecimal amount, String remark) {
        Card card = cardRepository.findById(cardId).orElse(null);

        if (card == null) {
            return null;
        }

        BigDecimal oldBalance = card.getBalance();
        if (oldBalance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        BigDecimal newBalance = oldBalance.subtract(amount);
        card.setBalance(newBalance);
        card.setUpdatedAt(new Date());

        Card savedCard = cardRepository.save(card);

        CardRecord record = new CardRecord();
        record.setCard(card);
        record.setCardNo(card.getCardNo());
        record.setType("消费");
        record.setAmount(amount);
        record.setBalanceAfter(newBalance);
        record.setRemark(remark);
        cardRecordRepository.save(record);

        return savedCard;
    }

    @Override
    public List<CardRecord> getCardRecords(Integer cardId) {
        return cardRecordRepository.findByCard_IdOrderByCreatedAtDesc(cardId);
    }

    @Override
    public List<CardRecord> getAllRecords() {
        return cardRecordRepository.findAll();
    }

    @Override
    public Map<String, Object> getCardStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        long totalCards = cardRepository.count();
        Long usingCards = cardRepository.countByStatus("使用中");
        Long unusedCards = cardRepository.countByStatus("未启用");
        Long scrapCards = cardRepository.countByStatus("报废");

        statistics.put("totalCards", totalCards);
        statistics.put("usingCards", usingCards != null ? usingCards : 0);
        statistics.put("unusedCards", unusedCards != null ? unusedCards : 0);
        statistics.put("scrapCards", scrapCards != null ? scrapCards : 0);

        return statistics;
    }

    @Override
    public Map<String, Object> getFinancialStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        BigDecimal totalRecharge = cardRecordRepository.sumAmountByType("充值");
        BigDecimal totalConsume = cardRecordRepository.sumAmountByType("消费");

        Date today = new Date();
        BigDecimal todayRecharge = cardRecordRepository.sumAmountByTypeAndDate("充值", today);
        BigDecimal todayConsume = cardRecordRepository.sumAmountByTypeAndDate("消费", today);

        statistics.put("totalRecharge", totalRecharge != null ? totalRecharge : BigDecimal.ZERO);
        statistics.put("totalConsume", totalConsume != null ? totalConsume : BigDecimal.ZERO);
        statistics.put("todayRecharge", todayRecharge != null ? todayRecharge : BigDecimal.ZERO);
        statistics.put("todayConsume", todayConsume != null ? todayConsume : BigDecimal.ZERO);

        return statistics;
    }

    @Override
    public Card getCardByStudentId(Integer studentId) {
        return cardRepository.findByStudent_Id(studentId).orElse(null);
    }
}
