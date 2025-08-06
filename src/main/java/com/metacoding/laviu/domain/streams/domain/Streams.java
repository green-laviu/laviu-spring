package com.metacoding.laviu.domain.streams.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "streams_tb")
@Entity
public class Streams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected Streams() {
    }
}
