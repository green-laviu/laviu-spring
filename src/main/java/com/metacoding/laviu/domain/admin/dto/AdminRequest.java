package com.metacoding.laviu.domain.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class AdminRequest {

    @Data
    public static class LoginDTO {
        @NotNull(message = "이메일을 입력해주세요")
        private String email;
        @NotNull(message = "비밀번호를 입력해주세요")
        private String password;
    }

    @Data
    public static class ProcessReportDTO {
        @NotNull(message = "요청 오류가 발생했습니다. 다시시도 해주십시오.")
        private String status;
    }
}
