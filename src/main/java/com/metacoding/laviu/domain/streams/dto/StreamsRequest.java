package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

public class StreamsRequest {
    @Data
    public static class StreamsVerifyDTO {
        private String app;
        @NotNull(message = "streamKey가 전달되지 못했습니다. 다시시도 해주십시오.")
        private String name;
    }

    @Data
    public static class ThumbnailUpdateDTO {
        private String thumbnailUrl;
    }

    //저장용으로 처음 요청받는 용도
    @Data
    public static class SaveDTO {
        @NotNull(message = "방송 제목을 입력해주십시오.")
        private String title;
        private List<String> hashtagList;

        public Streams toEntity(Users user) {
            return Streams.builder()
                    .title(title)
                    .status(StreamsStatus.PENDING) // 기본값
                    .streamer(user)
                    .viewerCount(0)
                    .build();
        }

    }

    @Data
    public static class UpdateDTO {
        @NotNull(message = "제목을 입력해주십시오.")
        private String title;
        private List<String> hashtagList;
    }

    @Data
    public static class SearchDTO {
        private String query;

    }


}
