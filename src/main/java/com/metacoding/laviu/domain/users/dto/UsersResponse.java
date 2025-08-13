package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

public class UsersResponse {


    @Data
    public static class ChannelInfoDTO {

        private Integer userId;                 // ch_daju
        private String streamer;               // 채널명
        private String profileImageUrl;    // 프로필 이미지
        private Long followerCount;         // 팔로워 수
        private Boolean isFollowing;       // 팔로우 여부


        public ChannelInfoDTO(Users user, Long followerCount, Boolean isFollowing) {
            this.userId = user.getId();
            this.streamer = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
            this.followerCount = followerCount;
            this.isFollowing = isFollowing;
        }
    }
}
