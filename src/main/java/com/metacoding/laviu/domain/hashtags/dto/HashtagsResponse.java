package com.metacoding.laviu.domain.hashtags.dto;

import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import lombok.Data;

public class HashtagsResponse {

    @Data
    public static class DTO {
        private Integer hashtagId;
        private String hashtagName;

        public DTO(Hashtags hashtags) {
            this.hashtagId = hashtags.getId();
            this.hashtagName = hashtags.getName();
        }
    }
}
