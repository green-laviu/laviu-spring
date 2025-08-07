package com.metacoding.laviu.domain.reports.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "reports_tb")
@Entity
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String snapshotStreamTitle;
    private String snapshotStreamerNickname;
    private String details;

    //Enum part
    @Enumerated(EnumType.STRING)
    private ReportsCategory category;
    @Enumerated(EnumType.STRING)
    private ReportsStatus status;

    //Date part
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    //FK(Foreign Key) part
    @ManyToOne
    private Users reporter;
    @ManyToOne
    private Streams reportedStream;
    @ManyToOne
    private Users reportedStreamer;

    // 기본생성자 사용금지
    protected Reports() {
    }
}
