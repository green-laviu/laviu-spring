package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.util.List;

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

    @Data
    public static class ThumbnailUpdateDTO {
        private String thumbnailUrl;
    }

    //저장용으로 처음 요청받는 용도
    @Data
    public static class SaveDTO {
        private String title;
        private List<String> hashtags;

        public SaveDTO(String title, List<String> hashtags) {
            this.title = title;
            this.hashtags = hashtags;
        }

        public Streams toEntity(Users user, String streamKey) {
            return Streams.builder()
                    .streamKey(streamKey)
                    .title(title)
                    .status(StreamsStatus.PENDING) // 기본값
                    .viewerCount(0)
                    .streamer(user)
                    .build();
        }

    }

}
