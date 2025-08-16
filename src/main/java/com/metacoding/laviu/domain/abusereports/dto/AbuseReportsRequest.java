package com.metacoding.laviu.domain.abusereports.dto;

import lombok.Data;

public class AbuseReportsRequest {

    @Data
    public static class saveDTO {

        private Integer categoryId;
        private String details;

    }
}
