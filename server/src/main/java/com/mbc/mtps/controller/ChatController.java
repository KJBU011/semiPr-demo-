package com.mbc.mtps.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.service.ChatService;

@RestController
public class ChatController {

    final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // AI 챗봇 문의 (부가 기능)
    // [수정] 개인 주차 정보 조회를 위해 id 파라미터 추가
    @PostMapping("chat")
    public String chat(String message, String id) {
        System.out.println("ChatController chat " + new Date());
        return chatService.askChatbot(message, id);
    }
}