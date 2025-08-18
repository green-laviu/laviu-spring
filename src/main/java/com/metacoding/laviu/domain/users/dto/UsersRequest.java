package com.metacoding.laviu.domain.users.dto;

import lombok.Data;

public class UsersRequest {

    @Data
    public static class updateDTO {
        private String username;
        private String channelDescription;
        private String profileImageUrl;

        public updateDTO(String username, String channelDescription, String profileImageUrl) {
            this.username = username;
            this.channelDescription = channelDescription;
            this.profileImageUrl = profileImageUrl;
        }

        public updateDTO() {
        }
    }

    @Data
    public static class SearchDTO {
        private String query;
    }
}
