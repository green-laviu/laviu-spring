package com.metacoding.laviu.domain.users.dto;

import lombok.Data;

public class UsersRequest {

    @Data
    public static class SearchDTO {
        private String query;
    }
}
