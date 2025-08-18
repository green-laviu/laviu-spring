package com.metacoding.laviu.domain.abusereports.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
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
    private AbuseReportCategorys category;

    // 기본생성자 사용금지
    protected AbuseReports() {
    }

    @Builder
    public AbuseReports(Integer id, String snapshotStreamTitle, String snapshotStreamerNickname, String details, AbuseReportsStatus status, Users abuseReporter, Streams abuseReportedStream, Users abuseReportedStreamer, AbuseReportCategorys category) {
        this.id = id;
        this.snapshotStreamTitle = snapshotStreamTitle;
        this.snapshotStreamerNickname = snapshotStreamerNickname;
        this.details = details;
        this.status = status;
        this.abuseReporter = abuseReporter;
        this.abuseReportedStream = abuseReportedStream;
        this.abuseReportedStreamer = abuseReportedStreamer;
        this.category = category;
    }

    public void updateStatus(AbuseReportsStatus status) {
        this.status = status;
        this.processedAt = LocalDateTime.now();
    }
}
