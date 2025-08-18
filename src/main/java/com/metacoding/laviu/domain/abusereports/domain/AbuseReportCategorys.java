package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "abuse_report_cartegorys_tb")
@Entity
public class AbuseReportCategorys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Builder
    public AbuseReportCategorys(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    protected AbuseReportCategorys() {

    }
}
