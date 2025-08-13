package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Builder;
import lombok.Data;

public class UsersResponse {

    @Data
    public static class MeDTO {
        private Me me;
        private StreamsResponse.StreamDTO live;

        @Data
        public static class Me {
            private Integer id;
            private String nickname;
            private String profileImageUrl;
            private Long followerCount;
            private Boolean isLive;

            @Builder
            public Me(Users user, Long followerCount, Boolean isLive) {
                this.id = user.getId();
                this.nickname = user.getNickname();
                this.profileImageUrl = user.getProfileImageUrl();
                this.followerCount = followerCount;
                this.isLive = isLive;
            }
        }

        public MeDTO(Me me, StreamsResponse.StreamDTO live) {
            this.me = me;
            this.live = live;
        }
    }

    @Data
    public static class StreamerDTO {
        private Streamer streamer;
        private StreamsResponse.StreamDTO liveStream;

        @Data
        public static class Streamer {
            private Integer streamerId;
            private String streamerName;
            private String streamerProfileImageUrl;
            private Long followerCount;
            private String introduction;
            private Boolean isFollowing;
            private Boolean isNotified;
            private StreamsStatus streamStatus;

            public Streamer(Users user, Long followerCount, Boolean isFollowing, StreamsStatus streamStatus) {
                this.streamerId = user.getId();
                this.streamerName = user.getNickname();
                this.streamerProfileImageUrl = user.getProfileImageUrl();
                this.followerCount = followerCount;
                this.introduction = user.getBio();
                this.isFollowing = isFollowing;
                this.streamStatus = streamStatus;
            }

            @Builder
            public Streamer(Users user, Long followerCount, Boolean isFollowing, Boolean isNotified, StreamsStatus streamStatus) {
                this(user, followerCount, isFollowing, streamStatus);
                this.isNotified = isNotified;
            }
        }

        public StreamerDTO(Streamer streamer, StreamsResponse.StreamDTO liveStream) {
            this.streamer = streamer;
            this.liveStream = liveStream;
        }
    }

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
}
