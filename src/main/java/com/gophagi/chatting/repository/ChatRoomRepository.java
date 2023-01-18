package com.gophagi.chatting.repository;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.gophagi.chatting.dto.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	// Redis
	private static final String CHAT_ROOMS = "CHAT_ROOM";
	private final RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, ChatRoom> opsHashChatRoom;

	@PostConstruct
	private void init() {
		opsHashChatRoom = redisTemplate.opsForHash();
	}

	public List<ChatRoom> findAllRoom() {
		return opsHashChatRoom.values(CHAT_ROOMS);
	}

	public ChatRoom findRoomById(String id) {
		return opsHashChatRoom.get(CHAT_ROOMS, id);
	}

	/**
	 * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
	 */
	public ChatRoom createChatRoom(String name) {
		ChatRoom chatRoom = ChatRoom.create(name);
		opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
		return chatRoom;
	}
}