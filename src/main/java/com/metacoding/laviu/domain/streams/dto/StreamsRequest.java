package com.metacoding.laviu.domain.streams.dto;

import lombok.Data;

public class StreamsRequest {
    @Data
    public static class StreamsVerifyDTO {
        private String app;
        private String name;
        private String args;
        private String addr;
        private String clientId;
        private String tcUrl;
    }
}
