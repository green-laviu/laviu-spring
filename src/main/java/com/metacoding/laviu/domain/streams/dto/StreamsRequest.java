package com.metacoding.laviu.domain.streams.dto;

import lombok.Data;

public class StreamsRequest {
    @Data
    public static class StreamsVerifyDTO {
        private String name;
        private String args;
    }
}
