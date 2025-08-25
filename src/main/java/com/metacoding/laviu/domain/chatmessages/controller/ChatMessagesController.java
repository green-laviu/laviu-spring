package com.metacoding.laviu.domain.chatmessages.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.chatmessages.dto.*;
import com.metacoding.laviu.domain.chatmessages.service.ChatMessagesService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.service.ViewerSanctionsService;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessagesController {

    private final ViewerSanctionsService viewerSanctionsService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ViewersService viewersService;
    private final ChatMessagesService chatMessagesService;

    // 사용자가 채팅방에 참여했을 때 호출
    @MessageMapping("/streams/{streamKey}/join")
    public void handleJoin(@DestinationVariable String streamKey, SimpMessageHeaderAccessor headerAccessor) {
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();

        log.debug("{}이 방송 채널에 참가 신청하였습니다", user.getNickname());
        // 1. 참가자 목록에 추가 (비즈니스 로직)
        viewersService.save(streamKey, user);

        // 2. 세션에 "어떤 방에 참여했는지" 정보 저장 (퇴장 시 사용)
        headerAccessor.getSessionAttributes().put("streamKey", streamKey);

        // 3. 스트리머에게만 최신 참가자 목록 전송
        updateAndSendParticipantList(streamKey);

        // 4. 방에 참가한 모든 유저에게 참가 알림

        log.debug("{}이 방송 채널에 참가 완료되었습니다", user.getNickname());

    }

    // 채팅 메시지 처리
    @MessageMapping("/streams/{streamKey}/chats")
    public void handleChatMessage(@DestinationVariable String streamKey, ChatMessagesRequest.wsSaveDTO reqDTO, SimpMessageHeaderAccessor headerAccessor) {
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();

        log.debug("{}이 채팅을 보냈습니다", user.getNickname());
        chatMessagesService.checkSanctions(streamKey, user);
        chatMessagesService.save(streamKey, user, reqDTO);
        List<ChatMessagesResponse.wsBroadcastDTO> respDTO = chatMessagesService.getChatListWithStreamKey(streamKey);

        messagingTemplate.convertAndSend("/sub/streams/" + streamKey + "/chats", respDTO);
    }

    // 제재 메시지 처리
    @MessageMapping("/streams/{streamKey}/sanctions")
    public void handleSanction(@DestinationVariable String streamKey, SanctionRequestDTO reqDTO, SimpMessageHeaderAccessor headerAccessor) {
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();

        log.debug("{}이 유저 id{}에게 {} 제재를 요청했습니다", user.getNickname(), reqDTO.getSanctionedUserId(), reqDTO.getSanctionType());

        SanctionResponseDTO sanctionResponseDTO = viewerSanctionsService.save(streamKey, user, reqDTO);
        WsNotificationResponseDTO respDTO = new WsNotificationResponseDTO("SANCTIONS", sanctionResponseDTO);

        // 제재 대상에게
        messagingTemplate.convertAndSendToUser(respDTO.getData().getSanctionedUserEmail(), "/queue/notifications", respDTO);
        // 스트리머에게
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", respDTO);

    }

    // 참가자 목록을 보내는 헬퍼 메서드
    public void updateAndSendParticipantList(String streamKey) {
        var participantList = viewersService.getList(streamKey);
        // 스트리머 전용 구독 주소로 메시지 전송
        messagingTemplate.convertAndSend("/sub/streams/" + streamKey + "/participants", participantList);
    }

    @GetMapping("/s/api/v1/streams/{streamId}/chats")
    public ResponseEntity<?> getChatList(@PathVariable Integer streamId) {
        log.debug("채팅목록 요청");
        List<ChatMessagesResponse.wsBroadcastDTO> respDTO = chatMessagesService.getChatListWithStreamId(streamId);
        log.debug("채팅목록 응답");
        return Resp.ok(respDTO);
    }
}
