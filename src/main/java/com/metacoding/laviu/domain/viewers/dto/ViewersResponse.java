package com.metacoding.laviu.domain.viewers.dto;

import java.time.LocalDateTime;

public class ViewersResponse {

    public class LiveDetailDTO {

        private Integer id;               // 시청자 목록 고유 식별자
        private Integer streamId;         // 방송 ID (Streams 참조)
        private Integer userId;           // 유저 ID (Users 참조)
        private LocalDateTime connectedAt; // 접속 시간

    }


}
