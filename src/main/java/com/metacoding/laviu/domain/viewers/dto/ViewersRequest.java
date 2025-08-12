package com.metacoding.laviu.domain.viewers.dto;


import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import lombok.Data;

public class ViewersRequest {

    @Data
    public static class SaveDTO {
        private Integer streamId; // 어떤 방송에 접속했는지
        private Integer userId;   // 누가 접속했는지

        public Viewers toEntity(Streams stream, Users user) {
            return Viewers.builder()
                    .stream(stream)
                    .user(user)
                    .build();
        }
    }
}