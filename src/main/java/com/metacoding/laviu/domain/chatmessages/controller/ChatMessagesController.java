package com.metacoding.laviu.domain.chatmessages.controller;

import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesRequest;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.chatmessages.service.ChatMessagesService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ChatMessagesController {

    private SimpMessagingTemplate messagingTemplate;
    private ViewersService viewersService;
    private ChatMessagesService chatMessagesService;

    // 사용자가 채팅방에 참여했을 때 호출
    @MessageMapping("/streams/{streamKey}/join")
    public void handleJoin(@DestinationVariable String streamKey, SimpMessageHeaderAccessor headerAccessor) {
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();

        try {
            // 1. 참가자 목록에 추가 (비즈니스 로직)
            viewersService.save(streamKey, user);

            // 2. 세션에 "어떤 방에 참여했는지" 정보 저장 (퇴장 시 사용)
            headerAccessor.getSessionAttributes().put("streamKey", streamKey);

            // 3. 스트리머에게만 최신 참가자 목록 전송
            updateAndSendParticipantList(streamKey);
        } catch (ExceptionApi404 e) {
            // 나중에 추가해야함 TODO
            System.out.println("이미 참가중입니다");
        }

    }

    // 채팅 메시지 처리
    @MessageMapping("/streams/{streamKey}/chats")
    public void handleChatMessage(@DestinationVariable String streamKey, ChatMessagesRequest.wsSaveDTO reqDTO, SimpMessageHeaderAccessor headerAccessor) {
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();

        chatMessagesService.save(streamKey, user, reqDTO);
        List<ChatMessagesResponse.wsBroadcastDTO> respDTO = chatMessagesService.getChatList(streamKey);

        messagingTemplate.convertAndSend("/sub/streams/" + streamKey + "/chats", respDTO);
    }

    // [변경] 참가자 목록을 보내는 헬퍼 메서드
    public void updateAndSendParticipantList(String streamKey) {
        var participantList = viewersService.getList(streamKey);
        // 스트리머 전용 구독 주소로 메시지 전송
        messagingTemplate.convertAndSend("/sub/streams/" + streamKey + "/participants", participantList);
    }
}
