package com.metacoding.laviu.domain.abusereports.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class AbuseReportsRequest {

    @Data
    public static class saveDTO {

        private Integer categoryId;
        @NotNull(message = "사유를 입력해주십시오.")
        private String details;

    }
}
