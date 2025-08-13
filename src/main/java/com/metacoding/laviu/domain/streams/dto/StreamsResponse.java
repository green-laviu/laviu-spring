package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.viewers.dto.ViewersResponse;
import lombok.Data;

import java.util.List;

public class StreamsResponse {

    @Data
    public static class SaveDTO {
        private Integer id;          // streamId
        private String streamKey;          // UUID로 된 스트림 키
        private String title;              // 방송 제목
        private List<String> hashtags;     // ["소통", "게임"]
        private StreamsStatus status; // PENDING, LIVE, ENDED 등

        public SaveDTO(Streams stream, List<StreamHashtags> streamHashtags) {
            this.id = stream.getId();
            this.streamKey = stream.getStreamKey();
            this.title = stream.getTitle();
            this.hashtags = streamHashtags.stream()
                    .map(sh -> sh.getHashtag().getName())
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
        private Integer streamerId;
        private String streamerName;
        private String streamerProfileImageUrl;
        private String title;
        private Integer viewerCount;
        private String thumbnailUrl;
        private StreamsStatus status;
        private List<Hashtags> hashtags;

        public StreamDTO(Integer streamId, String streamKey, Integer streamerId, String streamerName, String streamerProfileImageUrl, String title, Integer viewerCount, String thumbnailUrl, StreamsStatus status, List<Hashtags> hashtags) {
            this.streamId = streamId;
            this.streamKey = streamKey;
            this.streamerId = streamerId;
            this.streamerName = streamerName;
            this.streamerProfileImageUrl = streamerProfileImageUrl;
            this.title = title;
            this.viewerCount = viewerCount;
            this.thumbnailUrl = thumbnailUrl;
            this.status = status;
            this.hashtags = hashtags;
        }
    }

    @Data
    public static class DetailDTO {
        private LiveDetailDTO live;
        private List<ChatMessagesResponse.ChatDetailDTO> chatList;
        private List<ViewersResponse.ViewersDetailDTO> viewerList;


        public DetailDTO(List<ChatMessagesResponse.ChatDetailDTO> chatList, LiveDetailDTO live, List<ViewersResponse.ViewersDetailDTO> viewerList) {
            this.chatList = chatList;
            this.live = live;
            this.viewerList = viewerList;
        }
    }


}
