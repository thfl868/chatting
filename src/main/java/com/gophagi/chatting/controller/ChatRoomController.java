package com.gophagi.chatting.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gophagi.chatting.dto.ChatRoom;
import com.gophagi.chatting.dto.LoginInfo;
import com.gophagi.chatting.repository.ChatRoomRepository;
import com.gophagi.chatting.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

	private final ChatRoomRepository chatRoomRepository;
	private final JwtTokenProvider jwtTokenProvider;

	// 채팅 리스트 화면
	@GetMapping("/room")
	public String rooms(Model model) {
		return "/chat/room";
	}

	// 모든 채팅방 목록 반환
	@GetMapping("/rooms")
	@ResponseBody
	public List<ChatRoom> room() {
		List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom();
		chatRooms.stream()
			.forEach(room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId())));
		return chatRooms;
	}

	// 채팅방 생성
	@PostMapping("/room")
	@ResponseBody
	public ChatRoom createRoom(@RequestParam String name) {
		return chatRoomRepository.createChatRoom(name);
	}

	// 채팅방 입장 화면
	@GetMapping("/room/enter/{roomId}")
	public String roomDetail(Model model, @PathVariable String roomId) {
		model.addAttribute("roomId", roomId);
		return "/chat/roomdetail";
	}

	// 특정 채팅방 조회
	@GetMapping("/room/{roomId}")
	@ResponseBody
	public ChatRoom roomInfo(@PathVariable String roomId) {
		return chatRoomRepository.findRoomById(roomId);
	}

	@GetMapping("/user")
	@ResponseBody
	public LoginInfo getUserInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return LoginInfo.builder().name(name).token(jwtTokenProvider.generateToken(name)).build();
	}
}