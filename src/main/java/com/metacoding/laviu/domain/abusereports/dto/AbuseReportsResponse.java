package com.metacoding.laviu.domain.abusereports.dto;

import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsStatus;
import lombok.Data;

import java.time.LocalDateTime;

public class AbuseReportsResponse {

    @Data
    public static class saveDTO {
        private Integer id;
        private String snapshotStreamTitle;
        private String snapshotStreamerNickname;
        private String details;
        private AbuseReportsStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;

        // 신고자
        private Integer abuseReporterId;
        private String abuseReporterNickname;

        // 스트림방송id
        private Integer abuseReportedStreamId;

        // 신고받은 스트리머
        private Integer abuseReportedStreamerId;
        private String abuseReportedStreamerNickname;

        // 카테고리
        private Integer categoryId;
        private String categoryTitle;


        public saveDTO(AbuseReports abuseReportPS) {
            this.id = abuseReportPS.getId();
            this.snapshotStreamTitle = abuseReportPS.getSnapshotStreamTitle();
            this.snapshotStreamerNickname = abuseReportPS.getSnapshotStreamerNickname();
            this.details = abuseReportPS.getDetails();
            this.status = abuseReportPS.getStatus();
            this.createdAt = abuseReportPS.getCreatedAt();
            this.processedAt = abuseReportPS.getProcessedAt();

            this.abuseReporterId = abuseReportPS.getAbuseReporter().getId();
            this.abuseReporterNickname = abuseReportPS.getAbuseReporter().getNickname();

            this.abuseReportedStreamId = abuseReportPS.getAbuseReportedStream().getId();

            this.abuseReportedStreamerId = abuseReportPS.getAbuseReportedStreamer().getId();
            this.abuseReportedStreamerNickname = abuseReportPS.getAbuseReportedStreamer().getNickname();

            this.categoryId = abuseReportPS.getCategory().getId();
            this.categoryTitle = abuseReportPS.getCategory().getTitle();
        }


    }
}
