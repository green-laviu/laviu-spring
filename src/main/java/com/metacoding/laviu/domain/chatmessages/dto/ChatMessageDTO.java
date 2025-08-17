package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageDTO {
    // 작성자 정보
    private Integer authorId;        // 작성자 PK
    private String authorNickname;   // 작성자 닉네임
    private String authorHandle;     // 이메일 @ 앞부분 (예: "user123")

    // 메시지 정보
    private String message;
    private LocalDateTime timestamp; // 보내줄때는 null, 응답할때는 db 시간
}

