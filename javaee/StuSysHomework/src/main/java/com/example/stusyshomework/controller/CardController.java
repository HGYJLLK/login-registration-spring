package com.example.stusyshomework.controller;

import com.example.stusyshomework.entity.Card;
import com.example.stusyshomework.entity.CardRecord;
import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.service.CardService;
import com.example.stusyshomework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/index")
    public String cardIndex() {
        return "card/index";
    }

    @GetMapping("/list")
    public String listCards(Model model) {
        List<Card> cards = cardService.getAllCards();
        model.addAttribute("cards", cards);
        return "card/list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Integer id, Model model) {
        if (id != null) {
            Card card = cardService.getCardById(id);
            model.addAttribute("card", card);
        }
        return "card/form";
    }

    @PostMapping("/save")
    public String saveCard(@RequestParam(required = false) Integer id,
                          @RequestParam String cardNo,
                          @RequestParam String status,
                          @RequestParam(required = false, defaultValue = "0") BigDecimal balance) {
        Card card;
        if (id != null) {
            card = cardService.getCardById(id);
        } else {
            card = new Card();
        }

        card.setCardNo(cardNo);
        card.setStatus(status);
        if (balance != null) {
            card.setBalance(balance);
        }

        cardService.saveCard(card);
        return "redirect:/card/list";
    }

    @GetMapping("/delete")
    public String deleteCard(@RequestParam Integer id) {
        cardService.deleteCard(id);
        return "redirect:/card/list";
    }

    @GetMapping("/detail")
    public String cardDetail(@RequestParam Integer id, Model model) {
        Card card = cardService.getCardById(id);
        List<CardRecord> records = cardService.getCardRecords(id);
        model.addAttribute("card", card);
        model.addAttribute("records", records);
        return "card/detail";
    }

    @GetMapping("/bind")
    public String showBindForm(@RequestParam Integer cardId, Model model) {
        Card card = cardService.getCardById(cardId);
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("card", card);
        model.addAttribute("students", students);
        return "card/bind";
    }

    @PostMapping("/bind")
    public String bindStudent(@RequestParam Integer cardId, @RequestParam Integer studentId) {
        try {
            cardService.bindStudent(cardId, studentId);
            return "redirect:/card/detail?id=" + cardId;
        } catch (Exception e) {
            return "redirect:/card/bind?cardId=" + cardId + "&error=" + e.getMessage();
        }
    }

    @GetMapping("/unbind")
    public String unbindStudent(@RequestParam Integer cardId) {
        cardService.unbindStudent(cardId);
        return "redirect:/card/detail?id=" + cardId;
    }

    @GetMapping("/recharge")
    public String showRechargeForm(@RequestParam Integer cardId, Model model) {
        Card card = cardService.getCardById(cardId);
        model.addAttribute("card", card);
        return "card/recharge";
    }

    @PostMapping("/recharge")
    public String recharge(@RequestParam Integer cardId,
                          @RequestParam BigDecimal amount,
                          @RequestParam(required = false) String remark) {
        try {
            cardService.recharge(cardId, amount, remark);
            return "redirect:/card/detail?id=" + cardId;
        } catch (Exception e) {
            return "redirect:/card/recharge?cardId=" + cardId + "&error=" + e.getMessage();
        }
    }

    @GetMapping("/consume")
    public String showConsumeForm(@RequestParam Integer cardId, Model model) {
        Card card = cardService.getCardById(cardId);
        model.addAttribute("card", card);
        return "card/consume";
    }

    @PostMapping("/consume")
    public String consume(@RequestParam Integer cardId,
                         @RequestParam BigDecimal amount,
                         @RequestParam(required = false) String remark) {
        try {
            cardService.consume(cardId, amount, remark);
            return "redirect:/card/detail?id=" + cardId;
        } catch (Exception e) {
            return "redirect:/card/consume?cardId=" + cardId + "&error=" + e.getMessage();
        }
    }

    @GetMapping("/records")
    public String showRecords(@RequestParam(required = false) Integer cardId, Model model) {
        List<CardRecord> records;
        if (cardId != null) {
            records = cardService.getCardRecords(cardId);
            Card card = cardService.getCardById(cardId);
            model.addAttribute("card", card);
        } else {
            records = cardService.getAllRecords();
        }
        model.addAttribute("records", records);
        return "card/records";
    }

    @GetMapping("/statistic")
    public String showStatistics(Model model) {
        Map<String, Object> cardStats = cardService.getCardStatistics();
        Map<String, Object> financialStats = cardService.getFinancialStatistics();
        model.addAttribute("cardStats", cardStats);
        model.addAttribute("financialStats", financialStats);
        return "card/statistic";
    }

    @GetMapping("/selectForStudent")
    public String selectCardForStudent(@RequestParam Integer studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        List<Card> availableCards = cardService.getCardsByStatus("未启用");
        model.addAttribute("student", student);
        model.addAttribute("cards", availableCards);
        return "card/selectForStudent";
    }

    @PostMapping("/bindToStudent")
    public String bindCardToStudent(@RequestParam Integer cardId, @RequestParam Integer studentId) {
        try {
            cardService.bindStudent(cardId, studentId);
            return "redirect:/student/list";
        } catch (Exception e) {
            return "redirect:/card/selectForStudent?studentId=" + studentId + "&error=" + e.getMessage();
        }
    }
}
