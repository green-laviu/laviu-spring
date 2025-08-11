package com.metacoding.laviu.domain.chatmessages.dto;

import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.time.LocalDateTime;

public class ChatMessagesResponse {

    @Data
    public static class ChatDetailDTO {
        private Integer  id;             // chat_001
        private Integer  streamId;
        private Integer  userId;// 작성자 닉네임
        private String content;        // 채팅 내용
        private LocalDateTime createdAt;


        public ChatDetailDTO(Integer id, int stream_id, int user_id, String content, LocalDateTime createdAt) {
            this.id = id;
            this.streamId = stream_id;
            this.userId = user_id;
            this.content = content;
            this.createdAt = createdAt;
        }
        public ChatMessages toEntity(Streams stream, Users user) {
            return ChatMessages.builder()
                    .stream(stream)
                    .user(user)
                    .content(this.content)
                    .build();
        }


    }
}
