package com.metacoding.laviu.domain.notifications.dto;

import com.metacoding.laviu.domain.notifications.domain.Notifications;
import com.metacoding.laviu.domain.streams.domain.Streams;
import lombok.Data;

import java.time.LocalDateTime;

public class NotificationsResponse {

    @Data
    public static class NotificationsListDto{

        private Integer notificationId;
        private Integer channelId;
        private String channelName;
        private String profileImageUrl;
        private String message;
        private String title;
        private LocalDateTime receivedAt;

        public NotificationsListDto(Notifications notification, Streams streamPS) {
            this.notificationId = notification.getId();
            this.channelId = streamPS.getId();
            this.channelName = streamPS.getStreamer().getNickname();
            this.profileImageUrl = streamPS.getStreamer().getProfileImageUrl();
            this.message = notification.getContent();
            this.title = streamPS.getTitle();
            this.receivedAt = notification.getCreatedAt();
        }
    }
}
