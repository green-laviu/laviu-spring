package com.metacoding.laviu.domain.hashtags.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import jakarta.persistence.*;
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
    private Streams stream;
    @ManyToOne
    private Hashtags hashtag;

    // 기본생성자 사용금지
    protected StreamHashtags() {
    }

    //해시태그 인서트 로직 생성시 삭제필요 -더미용 TODO
    public StreamHashtags(Streams stream, Hashtags hashtag) {
        this.stream = stream;
        this.hashtag = hashtag;
    }
}
