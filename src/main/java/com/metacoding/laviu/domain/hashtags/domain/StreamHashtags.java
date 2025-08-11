package com.metacoding.laviu.domain.hashtags.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "stream_hashtags_tb")
@Entity
public class StreamHashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //FK(Foreign Key) part
    @ManyToOne
    @JoinColumn(name = "stream_id")
    private Streams stream;
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtags hashtag;

    // 기본생성자 사용금지
    protected StreamHashtags() {
    }

    @Builder
    public StreamHashtags(Streams stream, Hashtags hashtag) {
        this.stream = stream;
        this.hashtag = hashtag;
    }
}
