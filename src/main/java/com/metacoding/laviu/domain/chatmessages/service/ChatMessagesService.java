package com.metacoding.laviu.domain.chatmessages.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu._core.error.ex.StompException403;
import com.metacoding.laviu._core.utils.CommonUtils;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesRequest;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctions;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final StreamsRepository streamsRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ViewerSanctionsRepository viewerSanctionsRepository;

    @Transactional
    public void save(String streamKey, Users user, ChatMessagesRequest.wsSaveDTO reqDTO) {
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
        chatMessagesRepository.save(chatMessages);
    }

    public List<ChatMessagesResponse.wsBroadcastDTO> getChatListWithStreamKey(String streamKey) {
        // 1. 채팅 목록 조회 (최신 30개)
        List<ChatMessages> chatMessageList = chatMessagesRepository.findLatest30ByStreamKeyJoinFetchUserAndStream(streamKey);

        // 다시 역순으로
        Collections.reverse(chatMessageList);

        // 2. 리스트 로 반환
        return chatMessageList.stream()
                .map(chatMessage -> {
                    Users author = chatMessage.getUser();
                    return ChatMessagesResponse.wsBroadcastDTO.builder()
                            .authorId(author.getId())
                            .authorNickname(author.getNickname())
                            .emailId(author.getEmail()) // 또는 getUsername()
                            .isStreamer(chatMessage.getStream().getStreamer().getId().equals(author.getId()))
                            .content(chatMessage.getContent()) // ◀ 클라이언트가 보낸 content 사용
                            .timestamp(chatMessage.getCreatedAt())
                            .build();
                })
                .toList();
    }

    public List<ChatMessagesResponse.wsBroadcastDTO> getChatListWithStreamId(Integer streamId) {

        List<ChatMessages> chatMessageList = chatMessagesRepository.findLatest30ByStreamIdJoinFetchUserAndStream(streamId);

        // 다시 역순으로
        Collections.reverse(chatMessageList);

        return chatMessageList.stream()
                .map(chatMessages -> ChatMessagesResponse.wsBroadcastDTO
                        .builder()
                        .authorId(chatMessages.getUser().getId())
                        .authorNickname(chatMessages.getUser().getNickname())
                        .emailId(CommonUtils.localPart(chatMessages.getUser().getEmail()))
                        .timestamp(chatMessages.getCreatedAt())
                        .isStreamer(chatMessages.getStream().getStreamer().getId().equals(chatMessages.getUser().getId()))
                        .content(chatMessages.getContent())
                        .build())
                .toList();
    }

    /**
     * 방송 종료 메시지를 클라이언트에게 전송
     *
     * @param streamKey 메시지를 보낼 방송의 고유 키
     */
    public void sendStreamForcedEndMessage(String streamKey) {
        // 메시지 전송 대상 웹소켓 주소
        String destination = "/sub/streams/" + streamKey + "/notifications";

        // 해당 주소를 구독 중인 모든 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend(destination,
                new AdminResponse.ChatNotifyOff("STREAM_TERMINATE", "관리자에 의해 방송이 종료 되었습니다.")
        );
    }

    // 제재 여부 확이 로직
    public void checkSanctions(String streamKey, Users users) {
        ViewerSanctions sanctionsPs =
                viewerSanctionsRepository.findByStreamKeyAndUserId(streamKey, users.getId())
                        .orElse(null);
        if (sanctionsPs != null) {
            checkSanctionsTime(sanctionsPs);
        }

    }

    private void checkSanctionsTime(ViewerSanctions sanctionsPs) {
        int count = sanctionsPs.getOffenseCount();
        if (count <= 0) return;
        LocalDateTime createdAt = sanctionsPs.getCreatedAt();
        if (createdAt == null) return;
        LocalDateTime target = createdAt.plusSeconds(30L * count);
        Duration diff = Duration.between(LocalDateTime.now(), target);
        // 예: 초 단위 차이
        if (diff.isPositive()) {
            throw new StompException403(String.valueOf(diff.getSeconds()), ErrorEnum.IS_ON_SANCTIONS);
        }
    }
}
