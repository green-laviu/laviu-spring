package com.metacoding.laviu.domain.notifications.dto;

import com.metacoding.laviu.domain.notifications.domain.Notifications;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.time.LocalDateTime;

public class NotificationsResponse {

    @Data
    public static class NotificationsListDto {

        private Integer notificationId;
        private StreamerDTO streamer;
        private String title;
        private String content;
        private LocalDateTime receivedAt;

        public NotificationsListDto(Notifications notification, Streams streamPS) {
            this.notificationId = notification.getId();
            this.streamer = new StreamerDTO(streamPS.getStreamer());
            this.content = notification.getContent();
            this.title = streamPS.getTitle();
            this.receivedAt = notification.getCreatedAt();
        }
    }

    @Data
    public static class StreamerDTO {
        private Integer userId;
        private String nickname;
        private String profileImageUrl;

        public StreamerDTO(Users user) {
            this.userId = user.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
        }
    }
}
