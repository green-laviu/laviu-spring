package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class SanctionRequestDTO {
    private Integer sanctionedUserId;
    // TODO : [창호] 제재 요청 노션 확인후 변경
    private String type; // chatBan, kick
}
