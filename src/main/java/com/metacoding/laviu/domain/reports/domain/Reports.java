package com.metacoding.laviu.domain.reports.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "reports_tb")
@Entity
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 기본생성자 사용금지
    protected Reports() {
    }
}
