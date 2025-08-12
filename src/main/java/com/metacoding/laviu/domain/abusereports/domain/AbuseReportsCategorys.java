package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "abuse_reports_cartegorys_tb")
@Entity
public class AbuseReportsCategorys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
}
