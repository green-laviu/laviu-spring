package com.metacoding.laviu.domain.streams.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    private Integer viewerCount = 0;

    //Enum part
    @Enumerated(EnumType.STRING)
    private StreamsStatus status;

    //Date part
    @CreationTimestamp
    private LocalDateTime startedAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime endedAt;

    //FK(Foreign Key) part
    @ManyToOne
    private Users streamer;

    // 기본생성자 사용금지
    @Builder
    public Streams(Integer id, String streamKey, String title, String thumbnailUrl, Integer viewerCount, StreamsStatus status, LocalDateTime startedAt, LocalDateTime updatedAt, LocalDateTime endedAt, Users streamer) {
        this.id = id;
        this.streamKey = streamKey;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.viewerCount = viewerCount;
        this.status = status;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
        this.endedAt = endedAt;
        this.streamer = streamer;
    }

    public void updateStatus(StreamsStatus status) {
        this.status = status;
    }

    protected Streams() {
    }
}
