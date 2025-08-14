package com.metacoding.laviu.domain.admin.dto;

import com.metacoding.laviu.domain.users.domain.Users;
import lombok.Data;

import java.time.LocalDateTime;

public class AdminResponse {

    @Data
    public static class LoginDTO {
        private Integer id;
        private String email;
        private String nickname;
        private String role;

        public static LoginDTO of(Users user) {
            LoginDTO dto = new LoginDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setNickname(user.getNickname());
            dto.setRole("ADMIN");
            return dto;
        }
    }

    @Data
    public static class StreamDTO {
        private Integer id;              // id //
        private String streamKey;        // 스트림키 //
        private String title;            // 방송제목 //
        private String thumbnailUrl;     // 썸네일 //
        private Integer viewerCount;     // 시청자수 //
        private String status;           // 상태 //
        private LocalDateTime startTime; // 시작시간 //
        private LocalDateTime endTime;   // 종료시간 //
        private Integer streamerId;      // 스트리머 id //
        private String streamerName;     // 스트리머명 //
    }

    @Data
    public static class UserListDTO {
    }

    @Data
    public static class ReportListDTO {
    }
}
