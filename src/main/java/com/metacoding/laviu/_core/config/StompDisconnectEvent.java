package com.metacoding.laviu._core.config;

import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private StreamsService streamsService;
    private SimpMessagingTemplate messagingTemplate;
    private ViewersService viewersService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        // [변경] 세션에서 userId와 streamKey를 가져오는 것은 동일
        // Users 객체 전체 꺼내기
        Authentication auth = (Authentication) headerAccessor.getUser();
        Users user = (Users) auth.getPrincipal();
        String streamKey = (String) headerAccessor.getSessionAttributes().get("streamKey");

        if (user != null && streamKey != null) {
            // [변경] 퇴장 로직 처리
            streamsService.removeParticipant(streamKey, userId);

            // [변경] 참가자 목록 업데이트 (새 DTO 사용)
            updateAndSendParticipantList(streamKey);
        }
    }
}
