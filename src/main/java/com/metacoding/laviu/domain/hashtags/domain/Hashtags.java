package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "hashtags_tb")
@Entity
public class Hashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name; // 유니크

    @OneToMany(mappedBy = "hashtag")
    private List<StreamHashtags> streamHashtags = new ArrayList<>();

    // 기본생성자 사용금지
    protected Hashtags() {
    }

    @Builder
    public Hashtags(String name) {
        this.name = name;
    }
}
