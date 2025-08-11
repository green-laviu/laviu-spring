package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

public class UsersResponse {


    @Data
    public static class LiveDetailDTO {

        private Integer id;                 // ch_daju
        private String nickname;               // 채널명
        private String profileImageUrl;    // 프로필 이미지
        private int followerCount;         // 팔로워 수
        private boolean isFollowing;       // 팔로우 여부


        public LiveDetailDTO(Users user, int followerCount, boolean isFollowing) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = profileImageUrl;
            this.followerCount = followerCount;
            this.isFollowing = isFollowing;
        }
    }
}
