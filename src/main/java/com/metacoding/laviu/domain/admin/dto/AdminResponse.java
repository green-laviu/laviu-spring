package com.metacoding.laviu.domain.admin.dto;

import com.metacoding.laviu._core.utils.DateTimeUtils;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

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
            private String startTime;                   // 방송 시작 시간 (yyyy-MM-dd HH:mm 형식)
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
                // DateTimeUtils를 사용해 포맷팅
                this.startTime = DateTimeUtils.format(streams.getStartedAt());
                // DateTimeUtils를 사용해 포맷팅 (null 체크 포함)
                this.endTime = DateTimeUtils.format(streams.getEndedAt());
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
            private String createdAt;                   // 계정 생성일 (yyyy-MM-dd HH:mm 형식)
            private String updatedAt;                   // 마지막 정보 수정일 (yyyy-MM-dd HH:mm 형식, 정보가 없으면 "-")
            private String lastLoginAt;                 // 마지막 로그인 시간 (yyyy-MM-dd HH:mm 형식, 정보가 없으면 "-")
            private String provider;                    // OAuth2 제공자 (예: GOOGLE)
            private String bio;                         // 자기소개

            public User(Users user) {
                this.id = user.getId();
                this.email = user.getEmail();
                this.nickname = user.getNickname();
                this.role = user.getRoles();
                // DateTimeUtils를 사용해 포맷팅
                this.createdAt = DateTimeUtils.format(user.getCreatedAt());
                this.updatedAt = DateTimeUtils.format(user.getUpdatedAt());
                this.lastLoginAt = DateTimeUtils.format(user.getLastLoginAt());
                this.provider = (user.getProvider() != null) ? user.getProvider().name() : "-";
                this.bio = (user.getBio() != null) ? user.getBio() : "-";
            }
        }
    }

    @Data
    public static class ReportListDTO {
        private String title; // 화면 제목
        private Map<String, Boolean> menu = new HashMap<>(); // 메뉴 상태
        private List<Report> reports; // 신고 리스트

        @Data
        public static class Report {
            private Integer id;                         // 신고 ID
            private Integer reporterId;                 // 신고자 ID
            private Integer reportedStreamId;           // 신고된 방송 ID
            private Integer reportedStreamerId;         // 신고된 스트리머 ID
            private String broadcastTitle;              // 신고된 방송 제목
            private String streamerNickname;            // 신고된 스트리머 닉네임
            private String detailReason;                // 신고 상세 사유
            private String category;                    // 신고 유형(카테고리)
            private String createdAt;                   // 접수 시간 (yyyy-MM-dd HH:mm 형식)
            private String processedAt;                 // 처리된 시간 (yyyy-MM-dd HH:mm 형식, 처리 전이면 "-")
            private String status;                      // 처리확인 (예: PENDING, RESOLVED)

            public Report(AbuseReports abuseReports) {
                this.id = abuseReports.getId();
                this.reporterId = abuseReports.getAbuseReporter().getId();
                this.reportedStreamId = abuseReports.getAbuseReportedStream().getId();
                this.reportedStreamerId = abuseReports.getAbuseReportedStreamer().getId();
                this.broadcastTitle = abuseReports.getSnapshotStreamTitle();
                this.streamerNickname = abuseReports.getSnapshotStreamerNickname();
                this.detailReason = abuseReports.getDetails();
                this.category = abuseReports.getCategory().getTitle();
                // DateTimeUtils를 사용해 포맷팅
                this.createdAt = DateTimeUtils.format(abuseReports.getCreatedAt());
                // DateTimeUtils를 사용해 포맷팅 (null 체크 포함)
                this.processedAt = DateTimeUtils.format(abuseReports.getProcessedAt());
                this.status = abuseReports.getStatus().name();
            }
        }
    }
}