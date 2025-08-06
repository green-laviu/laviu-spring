package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "viewers_tb")
@Entity
public class Viewers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected Viewers() {
    }
}
