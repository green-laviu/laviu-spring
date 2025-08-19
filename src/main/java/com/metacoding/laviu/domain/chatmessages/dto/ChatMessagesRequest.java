package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

public class ChatMessagesRequest {

    @Data
    public static class wsSaveDTO {
        private String content;
    }
}
