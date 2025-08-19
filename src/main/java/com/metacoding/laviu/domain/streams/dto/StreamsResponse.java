package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.hashtags.dto.HashtagsResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.Data;

import java.util.List;

public class StreamsResponse {

    @Data
    public static class SaveDTO {
        private Integer streamId;          // streamId
        private String streamKey;          // UUID로 된 스트림 키
        private String title;              // 방송 제목
        private List<HashtagsResponse.DTO> hashtagList;     // ["소통", "게임"]
        private StreamsStatus status; // PENDING, LIVE, ENDED 등

        public SaveDTO(Streams stream) {
            this.streamId = stream.getId();
            this.streamKey = stream.getStreamKey();
            this.title = stream.getTitle();
            this.hashtagList = stream.getStreamHashtagList().stream()
                    .map(sh -> new HashtagsResponse.DTO(sh.getHashtag()))
                    .toList();
            this.status = stream.getStatus();
        }

    }

    @Data
    public static class StreamListDTO {
        private List<StreamDTO> carousel;
        private List<StreamDTO> recommended;

        public StreamListDTO(List<StreamDTO> carousel, List<StreamDTO> recommended) {
            this.carousel = carousel;
            this.recommended = recommended;
        }
    }

    @Data
    public static class StreamDTO {
        private Integer streamId;
        private String streamKey;
        private UsersResponse.DTO streamer;
        private String title;
        private Integer viewerCount;
        private String thumbnailUrl;
        private StreamsStatus status;
        private List<HashtagsResponse.DTO> hashtagList;

        public StreamDTO(Streams stream) {
            this.streamId = stream.getId();
            this.streamKey = stream.getStreamKey();
            this.streamer = new UsersResponse.DTO(stream.getStreamer());
            this.title = stream.getTitle();
            this.viewerCount = stream.getViewerCount();
            this.thumbnailUrl = stream.getThumbnailUrl();
            this.status = stream.getStatus();
            this.hashtagList = stream.getStreamHashtagList().stream()
                    .map(sh -> new HashtagsResponse.DTO(sh.getHashtag()))
                    .toList();
        }
    }

    @Data
    public static class DetailDTO {
        private LiveDetailDTO live;
        private List<ChatMessagesResponse.wsBroadcastDTO> chatList;

        public DetailDTO(LiveDetailDTO live, List<ChatMessages> chatMessageList) {
            this.live = live;
            this.chatList = chatMessageList.stream()
                    .map(chatMessage -> {
                        Users author = chatMessage.getUser();
                        return ChatMessagesResponse.wsBroadcastDTO.builder()
                                .authorId(author.getId())
                                .authorNickname(author.getNickname())
                                .emailId(author.getEmail()) // 또는 getUsername()
                                .isStreamer(chatMessage.getStream().getStreamer().getId().equals(author.getId()))
                                .content(chatMessage.getContent()) // ◀ 클라이언트가 보낸 content 사용
                                .timestamp(chatMessage.getCreatedAt())
                                .build();
                    })
                    .toList();
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer streamId;          // streamId
        private String streamKey;          // UUID로 된 스트림 키
        private String title;              // 방송 제목
        private List<HashtagsResponse.DTO> hashtagList;     // ["소통", "게임"]
        private StreamsStatus status; // PENDING, LIVE, ENDED 등

        public UpdateDTO(Streams stream) {
            this.streamId = stream.getId();
            this.streamKey = stream.getStreamKey();
            this.title = stream.getTitle();
            this.hashtagList = stream.getStreamHashtagList().stream()
                    .map(sh -> new HashtagsResponse.DTO(sh.getHashtag()))
                    .toList();
            this.status = stream.getStatus();
        }
    }


}
