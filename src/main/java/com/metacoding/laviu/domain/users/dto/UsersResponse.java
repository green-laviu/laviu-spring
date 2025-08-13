package com.metacoding.laviu.domain.users.dto;

import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class UsersResponse {

    @Data
    public static class MeDTO {
        private Me me;
        private Live live;

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

        @Data
        public static class Live {
            private Integer id;
            private String title;
            private String thumbnailUrl;
            private Long viewers;
            private List<Hashtags> badges;

            @Builder
            public Live(Streams streams, Long viewers, List<Hashtags> badges) {
                this.id = streams.getId();
                this.title = streams.getTitle();
                thumbnailUrl = streams.getThumbnailUrl();
                this.viewers = viewers;
                this.badges = badges;
            }
        }

        public MeDTO(Me me, Live live) {
            this.me = me;
            this.live = live;
        }
    }

    @Data
    public static class StreamerDTO {
        private Streamer streamer;
        private LiveStream liveStream;

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

        @Data
        public static class LiveStream {
            private Integer streamId;
            private String streamKey;
            private String title;
            private Integer viewerCount;
            private StreamsStatus streamStatus;
            private String thumbnailUrl;
            private List<Hashtags> hashtags;

            public LiveStream(Streams stream, List<Hashtags> badges) {
                this.streamId = stream.getId();
                this.streamKey = stream.getStreamKey();
                this.title = stream.getTitle();
                this.viewerCount = stream.getViewerCount();
                this.streamStatus = stream.getStatus();
                this.thumbnailUrl = stream.getThumbnailUrl();
                this.hashtags = badges;
            }

            public LiveStream(StreamsStatus streamStatus) {
                this.streamStatus = streamStatus;
            }
        }

        public StreamerDTO(Streamer streamer, LiveStream liveStream) {
            this.streamer = streamer;
            this.liveStream = liveStream;
        }
    }

    @Data
    public static class ChannelInfoDTO {
        private Integer id;                 // ch_daju
        private String nickname;               // 채널명
        private String profileImageUrl;    // 프로필 이미지
        private Long followerCount;         // 팔로워 수
        private Boolean isFollowing;       // 팔로우 여부

        public ChannelInfoDTO(Users user, Long followerCount, boolean isFollowing) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
            this.followerCount = followerCount;
            this.isFollowing = isFollowing;
        }
    }
}
