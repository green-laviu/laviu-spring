package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.hashtags.dto.HashtagsResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveDetailDTO {
    private Integer streamId;                // stream id
    private String title;             // 방송 제목
    private UsersResponse.ChannelInfoDTO channel;       // 유저 + 팔로어
    private String hlsUrl;            // HLS 주소
    private Integer viewerCount;               // 시청자 수
    private List<HashtagsResponse.DTO> hashtagList;         // 태그
    private LocalDateTime startedAt;          // 방송 시작 시각 (2025-08-12T14:00:00)
    private String streamKey;


    public LiveDetailDTO(Streams stream, UsersResponse.ChannelInfoDTO channel, String hlsUrl) {
        this.streamId = stream.getId();
        this.title = stream.getTitle();
        this.channel = channel;
        this.hlsUrl = hlsUrl;
        this.viewerCount = stream.getViewerCount();
        this.hashtagList = stream.getStreamHashtagList().stream()
                .map(sh -> new HashtagsResponse.DTO(sh.getHashtag()))
                .toList();
        this.startedAt = stream.getStartedAt();
        this.streamKey = stream.getStreamKey();
    }
}
