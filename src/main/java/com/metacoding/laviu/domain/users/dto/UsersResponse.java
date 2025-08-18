package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

public class UsersResponse {


    @Data
    public static class ChannelInfoDTO {

        private DTO streamer;
        private Long followerCount;         // 팔로워 수
        private Boolean isFollowing;       // 팔로우 여부

        public ChannelInfoDTO(Users user, Long followerCount, Boolean isFollowing) {
            this.streamer = new DTO(user);
            this.followerCount = followerCount;
            this.isFollowing = isFollowing;
        }
    }

    @Data
    public static class DTO {
        private Integer userId;
        private String nickname;
        private String profileImageUrl;
        private String email;
        private String bio;

        public DTO(Users users) {
            this.userId = users.getId();
            this.nickname = users.getNickname();
            this.profileImageUrl = users.getProfileImageUrl();
            this.email = users.getEmail();
            this.bio = users.getBio();
        }
    }

    @Data
    public static class SearchDTO {
        private Integer userId;
        private String nickname;
        private String profileImageUrl;
        private Long followerCount;
        private FollowStatusDTO FollowStatus;

        public SearchDTO(Users users, Long followerCount, FollowStatusDTO followStatus) {
            this.userId = users.getId();
            this.nickname = users.getNickname();
            this.profileImageUrl = users.getProfileImageUrl();
            this.followerCount = followerCount;
            this.FollowStatus = followStatus;
        }
    }

    @Data
    public static class FollowStatusDTO {
        private Integer followId; // FollowsPK
        private Boolean isFollowing; // 팔로워 여부

        public FollowStatusDTO(Integer followId, Boolean isFollowing) {
            this.followId = followId;
            this.isFollowing = isFollowing;
        }
    }
}
