package com.metacoding.laviu.domain.abusereports.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "abuse_reports_tb")
@Entity
public class AbuseReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String snapshotStreamTitle;
    private String snapshotStreamerNickname;
    private String details;

    //Enum part
    @Enumerated(EnumType.STRING)
    private AbuseReportsStatus status;

    //Date part
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    //FK(Foreign Key) part
    @ManyToOne
    private Users abuseReporter;
    @ManyToOne
    private Streams abuseReportedStream;
    @ManyToOne
    private Users abuseReportedStreamer;
    @ManyToOne
    private AbuseReportsCategorys category;

    // 기본생성자 사용금지
    protected AbuseReports() {
    }
}
