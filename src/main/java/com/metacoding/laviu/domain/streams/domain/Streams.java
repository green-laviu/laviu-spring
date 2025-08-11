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
    protected Streams() {
    }

    //save 빌더
    @Builder
    public Streams(String streamKey, String title, String thumbnailUrl,
                   StreamsStatus status, Integer viewerCount, Users streamer) {
        this.streamKey = streamKey;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.viewerCount = viewerCount;
        this.streamer = streamer;
    }
}
