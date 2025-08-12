package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

public class UsersResponse {


    @Data
    public static class ChannelInfoDTO {

        private Integer id;                 // ch_daju
        private String nickname;               // 채널명
        private String profileImageUrl;    // 프로필 이미지
        private Long followerCount;         // 팔로워 수
        private boolean isFollowing;       // 팔로우 여부


        public ChannelInfoDTO(Users user, Long followerCount, boolean isFollowing) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
            this.followerCount = followerCount;
            this.isFollowing = isFollowing;
        }
    }
}
