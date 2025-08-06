package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "follows_tb")
@Entity
public class Follows {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected Follows() {
    }
}
