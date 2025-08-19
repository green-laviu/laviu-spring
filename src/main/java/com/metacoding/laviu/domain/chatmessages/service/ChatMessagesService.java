package com.metacoding.laviu.domain.chatmessages.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesRequest;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final StreamsRepository streamsRepository;

    @Transactional
    public List<ChatMessagesResponse.wsBroadcastDTO> save(String streamKey, Users user, ChatMessagesRequest.wsSaveDTO reqDTO) {
        // 1. 방송 조회
        Streams streamPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        // 2. 채팅 엔티티 생성
        ChatMessages chatMessages = ChatMessages.builder()
                .stream(streamPS)
                .user(user)
                .content(reqDTO.getContent())
                .build();

        // 3. 채팅 저장
        ChatMessages chatMessagePS = chatMessagesRepository.save(chatMessages);

        // 4. 채팅 목록 조회 (최신 30개)
        List<ChatMessages> chatMessageList = chatMessagesRepository.findLatest30ByStreamKeyJoinFetchUser(streamKey);

        // 5. 리스트 로 반환
        return chatMessageList.stream()
                .map(chatMessage -> {
                    Users author = chatMessage.getUser();
                    return ChatMessagesResponse.wsBroadcastDTO.builder()
                            .authorId(author.getId())
                            .authorNickname(author.getNickname())
                            .emailId(author.getEmail()) // 또는 getUsername()
                            .isStreamer(streamPS.getStreamer().getId().equals(author.getId()))
                            .content(reqDTO.getContent()) // ◀ 클라이언트가 보낸 content 사용
                            .timestamp(chatMessagePS.getCreatedAt())
                            .build();
                })
                .toList();
    }
}
