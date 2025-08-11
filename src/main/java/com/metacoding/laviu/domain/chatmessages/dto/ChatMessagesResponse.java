package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class ChatMessagesResponse {

    @Data
    public static class ChatDetailDTO {
        private Integer id;             // chat_001
        private int stream_id;
        private int user_id;// 작성자 닉네임
        private String content;        // 채팅 내용
        private LocalDateTime createdAt;

    }
}
