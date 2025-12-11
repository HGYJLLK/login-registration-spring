package com.example.stusyshomework.controller;

import com.example.stusyshomework.listener.ChatMessageListener;
import com.example.stusyshomework.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/chat/config")
    public String configPage() {
        return "config";
    }

    @GetMapping("/chat/dialog")
    public String dialogPage(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "dialog";
    }

    @PostMapping("/api/chat/send")
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestParam("senderId") String senderId,
                                           @RequestParam("messageContent") String messageContent) {
        Map<String, Object> response = new HashMap<>();
        try {
            messageProducer.sendChatMessage(senderId, messageContent);
            response.put("success", true);
            response.put("message", "消息发送成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "消息发送失败: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/chat/messages")
    @ResponseBody
    public Map<String, Object> getMessages(@RequestParam(value = "lastIndex", defaultValue = "0") int lastIndex) {
        Map<String, Object> response = new HashMap<>();
        List<String> allMessages = ChatMessageListener.MESSAGE_LOG;

        int totalSize = allMessages.size();
        if (lastIndex < totalSize) {
            List<String> newMessages = allMessages.subList(lastIndex, totalSize);
            response.put("messages", newMessages);
            response.put("lastIndex", totalSize);
        } else {
            response.put("messages", List.of());
            response.put("lastIndex", lastIndex);
        }

        return response;
    }
}
