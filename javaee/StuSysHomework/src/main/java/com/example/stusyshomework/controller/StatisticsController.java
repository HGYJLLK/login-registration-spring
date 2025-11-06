package com.example.stusyshomework.controller;

import com.example.stusyshomework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class StatisticsController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        Map<String, Object> stats = studentService.getGenderStatistics();
        model.addAttribute("stats", stats);
        return "statistics";
    }
}