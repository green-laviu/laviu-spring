package com.metacoding.laviu.domain.streams.dto;

import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.Data;

import java.util.List;

@Data
public class LiveDetailDTO {
    private String id;                // live_20250807_01
    private String title;             // 방송 제목
    private UsersResponse.LiveDetailDTO channel;       // 유저 + 팔로어
    private String hlsUrl;            // HLS 주소
    private int viewers;               // 시청자 수
    private List<StreamHashtags> tags;         // 태그
    private String startedAt;          // 방송 시작 시각 (ISO8601)
    private List<QualityOptionDTO> qualityOptions = QualityOptionDTO.getAllOptions(); // 화질 옵션
    //private boolean isStreaming;

}
