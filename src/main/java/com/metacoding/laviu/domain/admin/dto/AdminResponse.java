package com.metacoding.laviu.domain.admin.dto;

import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminResponse {

    @Data
    public static class LoginDTO {
        private Integer id;         // 유저 ID
        private String email;       // 유저 이메일
        private String nickname;    // 유저 닉네임
        private String role;        // 유저 역할 (항상 "ADMIN"으로 고정)

        public LoginDTO(Users user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.role = "ADMIN";
        }
    }

    /**
     * 실시간 방송 관리 화면 전용 DTO (페이지 레벨)
     */
    @Data
    public static class StreamListDTO {
        private String title;                           // 페이지 타이틀 (예: "실시간 방송 관리")
        private Map<String, Boolean> menu = new HashMap<>(); // 사이드 메뉴 활성화 상태 관리
        private List<Stream> streams;                   // 스트림 목록 리스트

        @Data
        public static class Stream {
            private Integer id;                         // 스트림 ID
            private String streamKey;                   // 스트림 키 (고유 식별자)
            private String title;                       // 스트림 제목
            private String thumbnailUrl;                // 썸네일 이미지 URL
            private Integer viewerCount;                // 현재 시청자 수
            private String status;                      // 스트림 상태 (예: LIVE, ENDED)
            private LocalDateTime startTime;            // 방송 시작 시간
            private String endTime;                     // 방송 종료 시간 (방송 중일 경우 "-")
            private Integer streamerId;                 // 스트리머 ID
            private String streamerName;                // 스트리머 닉네임

            public Stream(Streams streams) {
                this.id = streams.getId();
                this.streamKey = streams.getStreamKey();
                this.title = streams.getTitle();
                this.thumbnailUrl = streams.getThumbnailUrl();
                this.viewerCount = streams.getViewerCount();
                this.status = streams.getStatus().name();
                this.startTime = streams.getStartedAt();
                this.endTime = (streams.getEndedAt() != null) ? streams.getEndedAt().toString() : "-"; // 종료시간이 없음 "-" 표시
                this.streamerId = streams.getStreamer().getId();
                this.streamerName = streams.getStreamer().getNickname();
            }
        }
    }

    @Data
    public static class UserListDTO {
        private String title;                           // 페이지 타이틀 (예: "유저 관리")
        private Map<String, Boolean> menu = new HashMap<>(); // 사이드 메뉴 활성화 상태 관리
        private List<User> users;                   // 유저 목록 리스트

        @Data
        public static class User {
            private Integer id;                         // 유저 ID
            private String email;                       // 유저 이메일
            private String nickname;                    // 유저 닉네임
            private String role;                        // 유저 역할 (ADMIN 또는 USER)
            private LocalDateTime createdAt;            // 계정 생성일
            private String updatedAt;                   // 마지막 정보 수정일
            private String lastLoginAt;                 // 마지막 로그인 시간
            private String provider;                    // OAuth2 제공자 (예: GOOGLE)
            private String bio;                         // 자기소개
            private String type;                        // 유저 타입 (예: USER, ADMIN)

            public User(Users user) {
                this.id = user.getId();
                this.email = user.getEmail();
                this.nickname = user.getNickname();
                this.role = user.getType().name();
                this.createdAt = user.getCreatedAt();
                // ADMIN의 경우 null이 들어올 수 있다. 그래서 "-" 출력
                this.updatedAt = (user.getUpdatedAt() != null) ? user.getUpdatedAt().toString() : "-";
                this.lastLoginAt = (user.getLastLoginAt() != null) ? user.getLastLoginAt().toString() : "-";
                this.provider = (user.getProvider() != null) ? user.getProvider().name() : "-";
                this.bio = (user.getBio() != null) ? user.getBio() : "-";
                this.type = user.getType().name();
            }
        }
    }

    @Data
    public static class ReportListDTO {
        private String title;
        private Map<String, Boolean> menu = new HashMap<>();
        private List<Report> reports;

        @Data
        public static class Report {
            private Integer id;
            private Integer reporterId; // 신고자 ID
            private Integer reportedStreamId; // 신고된 방송 ID
            private Integer reportedStreamerId; // 신고된 스트리머 ID
            private String broadcastTitle; // 신고된 방송 제목
            private String streamerNickname; // 신고된 스트리머 닉네임
            private String detailReason; // 신고 상세 사유
            private String reportType; // 신고 유형 (카테고리)
            private LocalDateTime createdAt;
            private String status;

            public Report(AbuseReports abuseReport) {
                this.id = abuseReport.getId();
                this.reporterId = abuseReport.getAbuseReporter().getId();
                this.reportedStreamId = abuseReport.getAbuseReportedStream().getId();
                this.reportedStreamerId = abuseReport.getAbuseReportedStreamer().getId();
                this.broadcastTitle = abuseReport.getSnapshotStreamTitle();
                this.streamerNickname = abuseReport.getSnapshotStreamerNickname();
                this.detailReason = abuseReport.getDetails();
                this.reportType = abuseReport.getCategory().getTitle();
                this.createdAt = abuseReport.getCreatedAt();
                this.status = abuseReport.getStatus().name();
            }
        }
    }

}
