package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveDetailDTO {
    private Integer id;                // stream id
    private String title;             // 방송 제목
    private UsersResponse.ChannelInfoDTO channel;       // 유저 + 팔로어
    private String hlsUrl;            // HLS 주소
    private Integer viewers;               // 시청자 수
    private List<StreamHashtags> hashtagList;         // 태그
    private LocalDateTime startedAt;          // 방송 시작 시각 (ISO8601)
    private List<QualityOptionDTO> qualityOptions = QualityOptionDTO.getAllOptions(); // 화질 옵션
    //private boolean isStreaming;


    public LiveDetailDTO(Streams stream, UsersResponse.ChannelInfoDTO channel, String hlsUrl, List<StreamHashtags> streamHashtagList) {
        this.id = stream.getId();
        this.title = stream.getTitle();
        this.channel = channel;
        this.hlsUrl = hlsUrl;
        this.viewers = stream.getViewerCount();
        this.hashtagList = streamHashtagList;
        this.startedAt = stream.getStartedAt();
    }
}
