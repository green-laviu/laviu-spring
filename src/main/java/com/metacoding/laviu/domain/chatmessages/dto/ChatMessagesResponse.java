package com.metacoding.laviu.domain.chatmessages.dto;

import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.metacoding.laviu._core.utils.CommonUtils.localPart;

public class ChatMessagesResponse {

    @NoArgsConstructor
    @Data
    public static class ChatDetailDTO {
        private Integer id;
        private String email;           // user mail 에서 @ 앞부분
        private String nickname;       // 작성자 닉네임
        private String content;        // 채팅 내용
        private LocalDateTime createdAt; //생성일자


        // 필요한 정보만 변경
        public static ChatDetailDTO from(ChatMessages chatMessage) {
            ChatDetailDTO dto = new ChatDetailDTO();
            dto.id = chatMessage.getId();
            dto.email = localPart(chatMessage.getUser().getEmail());
            dto.nickname = chatMessage.getUser().getNickname();
            dto.content = chatMessage.getContent();
            dto.createdAt = chatMessage.getCreatedAt();
            return dto;
        }

        //엔티티를 List dto로 변환 하기
        public static List<ChatDetailDTO> fromList(List<ChatMessages> list) {
            return list.stream().map(ChatDetailDTO::from).toList(); // JDK16+ (JDK8이면 Collectors.toList())
        }


    }
}
