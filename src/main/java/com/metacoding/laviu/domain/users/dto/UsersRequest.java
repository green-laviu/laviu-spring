package com.metacoding.laviu.domain.users.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class UsersRequest {

    @Data
    public static class updateDTO {
        @NotNull(message = "사용자명은 공백으로 바꿀 수 없습니다.")
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
