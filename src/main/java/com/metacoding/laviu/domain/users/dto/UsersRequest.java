package com.metacoding.laviu.domain.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Data
    @NoArgsConstructor
    public static class LoginDTO {
        @NotBlank(message = "AccessToken은 필수입니다.")
        private String accessToken;

        public LoginDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
