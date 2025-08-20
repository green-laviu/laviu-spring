package com.metacoding.laviu.domain.streams.domain;

import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Table(name = "streams_tb")
@Entity
public class Streams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String streamKey;
    private String title;
    private String thumbnailUrl;
    @Column(nullable = false)
    private Integer viewerCount;

    //Enum part
    @Enumerated(EnumType.STRING)
    private StreamsStatus status;

    //Date part
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime endedAt;

    //FK(Foreign Key) part
    @ManyToOne(fetch = FetchType.LAZY)
    private Users streamer;

    // 스트림 해시태그
    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StreamHashtags> streamHashtagList = new ArrayList<>();

    @Builder
    public Streams(String streamKey, String title, String thumbnailUrl, Integer viewerCount, StreamsStatus status, Users streamer) {
        this.streamKey = streamKey;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.viewerCount = viewerCount;
        this.status = status;
        this.streamer = streamer;
    }

    // 기본생성자 사용금지
    protected Streams() {
    }

    /**
     * 방송 시작 상태 변경 메서드
     */
    public void startLive() {
        this.startedAt = LocalDateTime.now();
        this.status = StreamsStatus.LIVE;
    }

    public void off() {
        this.endedAt = LocalDateTime.now();
        this.status = StreamsStatus.ENDED;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void downViewerCount() {
        this.viewerCount--;
    }

    public void upViewerCount() {
        this.viewerCount++;
    }

    public void updateInfo(String title, List<StreamHashtags> streamHashtagList) {
        this.title = title;
        this.streamHashtagList.clear();
        this.streamHashtagList.addAll(streamHashtagList);
        this.updatedAt = LocalDateTime.now();
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }
}
