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
    //uuid를 만들어서 저장용으로 조립하기 위한 용도
    @Data
    public class SaveAssembleDto {
        private String title;
        private String thumbnailUrl;
        private String streamKey; // UUID
        private Integer  userId; // 세션에서 꺼낸 ID

        public SaveAssembleDto(String title, String thumbnailUrl, String streamKey, Integer userId) {
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
            this.streamKey = streamKey;
            this.userId = userId;
        }
    }

}
