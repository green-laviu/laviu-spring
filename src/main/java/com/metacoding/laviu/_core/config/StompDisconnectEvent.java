package com.metacoding.laviu._core.config;

import com.metacoding.laviu.domain.chatmessages.controller.ChatMessagesController;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private ChatMessagesController chatMessagesController;
    private ViewersService viewersService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        // Users 객체 꺼내기
        Users user = null;
        if (headerAccessor.getUser() != null && headerAccessor.getUser() instanceof Authentication) {
            Authentication auth = (Authentication) headerAccessor.getUser();
            Object principal = auth.getPrincipal();
            if (principal instanceof Users) {
                user = (Users) principal;
            }
        }
        String streamKey = (String) headerAccessor.getSessionAttributes().get("streamKey");

        if (user != null && streamKey != null) {
            // [변경] 퇴장 로직 처리
            viewersService.delete(streamKey, user);
            System.out.println(user.getNickname() + " 퇴장");

            // [변경] 참가자 목록 업데이트 (새 DTO 사용). 임시로 컨트롤러 메서드 가져옴
            chatMessagesController.updateAndSendParticipantList(streamKey);
        }
    }
}
