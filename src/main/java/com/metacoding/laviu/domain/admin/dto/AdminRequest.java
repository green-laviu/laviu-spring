package com.metacoding.laviu.domain.admin.dto;

import lombok.Data;

public class AdminRequest {

    @Data
    public static class LoginDTO {
        private String email;
        private String password;
    }

    @Data
    public static class ProcessReportDTO {
        private String status;
    }
}
