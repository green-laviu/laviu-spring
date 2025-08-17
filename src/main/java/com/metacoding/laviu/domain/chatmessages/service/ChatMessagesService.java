package com.metacoding.laviu.domain.chatmessages.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessageDTO;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final StreamsRepository streamsRepository;

    public ChatMessageDTO save(String streamKey, Users user, ChatMessageDTO chatMessageDTO) {
        // 1. 방송 조회
        Streams streamPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        // 2. 채팅 엔티티 생성
        ChatMessages chatMessages = ChatMessages.builder()
                .stream(streamPS)
                .user(user)
                .content(chatMessageDTO.getMessage())
                .build();

        // 3. 채팅 저장
        ChatMessages chatMessagePS = chatMessagesRepository.save(chatMessages);

        // 4. 시간 등록
        chatMessageDTO.setTimestamp(chatMessagePS.getCreatedAt());

        // 5. 스트리머 유무
        chatMessageDTO.setIsStreamer(streamPS.getStreamer().getId().equals(user.getId()));

        return chatMessageDTO;
    }
}
