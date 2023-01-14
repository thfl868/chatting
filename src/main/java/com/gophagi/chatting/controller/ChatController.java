package com.gophagi.chatting.controller;

import com.gophagi.chatting.dto.ChatMessage;
import com.gophagi.chatting.dto.ChatRoom;
import com.gophagi.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if(ChatMessage.MessageType.JOIN.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}