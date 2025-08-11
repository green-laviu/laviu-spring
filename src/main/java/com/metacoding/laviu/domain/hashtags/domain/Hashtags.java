package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "hashtags_tb")
@Entity
public class Hashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name; // 유니크

    // 기본생성자 사용금지
    protected Hashtags() {
    }

    //해시태그 인서트 로직 생성시 삭제필요 -더미용 TODO
    public Hashtags(String name) {
        this.name = name;
    }
}
