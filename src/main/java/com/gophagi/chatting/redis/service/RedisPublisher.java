package com.gophagi.chatting.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.gophagi.chatting.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
	private final RedisTemplate<String, Object> redisTemplate;

	public void publish(ChannelTopic topic, ChatMessage message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}