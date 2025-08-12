package com.metacoding.laviu.domain.chatmessages.dto;

import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.time.LocalDateTime;

public class ChatMessagesRequest {

    @Data
    public static class saveDTO {
        private Integer streamId;     //스트리머 id
        private Integer userId;       // 작성자 id
        private String content;        // 채팅 내용
        private LocalDateTime createdAt;  //생성날짜


        // to entity
        public ChatMessages toEntity(Streams stream, Users user, String content) {
            return ChatMessages.builder()
                    .stream(stream)
                    .user(user)
                    .content(content)
                    .build();
        }


    }
}
