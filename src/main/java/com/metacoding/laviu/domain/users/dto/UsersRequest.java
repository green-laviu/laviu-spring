package com.metacoding.laviu.domain.users.dto;

import lombok.Data;

public class UsersRequest {

    @Data
    public static class updateDTO {
        private String username;
        private String channelDescription;
        private String profileImageUrl;
    }
}
