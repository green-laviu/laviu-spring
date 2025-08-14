package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Follows;
import lombok.Data;

public class FollowsResponse {

    @Data
    public static class SaveDTO {

        private Integer followId;
        private Integer followerId;
        private Integer followingId;
        private Boolean isFollowing;
        private Boolean isNotificationsEnabled;

        public SaveDTO(Follows follow) {
            this.followId = follow.getId();
            this.followerId = follow.getFollower().getId();
            this.followingId = follow.getFollowing().getId();
            this.isFollowing = true;
            this.isNotificationsEnabled = follow.getIsNotificationsEnabled();
        }

    }

    @Data
    public static class UpdateDTO {

        private Integer followId;
        private Integer followerId;
        private Integer followingId;
        private Boolean isFollowing;
        private Boolean isNotificationsEnabled;

        public UpdateDTO(Follows follow) {
            this.followId = follow.getId();
            this.followerId = follow.getFollower().getId();
            this.followingId = follow.getFollowing().getId();
            this.isFollowing = true;
            this.isNotificationsEnabled = follow.getIsNotificationsEnabled();
        }

    }

}
