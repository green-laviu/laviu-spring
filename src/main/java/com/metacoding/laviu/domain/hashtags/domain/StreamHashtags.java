package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "stream_hashtags_tb")
@Entity
public class StreamHashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected StreamHashtags() {
    }
}
