package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "abuse_report_cartegorys_tb")
@Entity
public class AbuseReportCategorys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
}
