package com.metacoding.laviu.domain.users.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class FollowsResponse {

    @Data
    public static class SaveDTO {

        private Integer id;
        private LocalDateTime followedAt;
        private Integer followerId;
        private String followerName;
        private Integer followingId;
        private String followingName;
    }


}
