package com.metacoding.laviu.domain.streams.dto;

import lombok.Data;

public class StreamsRequest {

    //저장용으로 처음 요청받는 용도
    @Data
    public static class SaveDto{
        private String title;
        private String thumbnailUrl;

        public SaveDto(String title, String thumbnailUrl) {
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
        }
    }

}
