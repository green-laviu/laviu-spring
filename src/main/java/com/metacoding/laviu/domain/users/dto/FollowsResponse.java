package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.users.domain.Follows;
import lombok.Data;

public class FollowsResponse {

    @Data
    public static class SaveDTO {

        private Integer id;
        private Integer followingId;
        private Boolean isFollowing;

        public SaveDTO(Follows followPS, boolean isFollowing) {
            this.id = followPS.getId();
            this.followingId = followPS.getFollowing().getId();
            this.isFollowing = isFollowing;
        }

    }

    @Data
    public static class deleteDTO {

        private Integer id;
        private Boolean isFollowing;


        public deleteDTO(Integer id, boolean isFollowing) {
            this.id = id;
            this.isFollowing = isFollowing;
        }
    }


}
