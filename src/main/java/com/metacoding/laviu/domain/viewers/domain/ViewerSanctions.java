package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "viewer_sanctions_tb")
@Entity
public class ViewerSanctions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected ViewerSanctions() {
    }
}
