package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "hashtags_tb")
@Entity
public class Hashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name; // 유니크

    // 기본생성자 사용금지
    protected Hashtags() {
    }

    @Builder
    public Hashtags(String name) {
        this.name = name;
    }
}
