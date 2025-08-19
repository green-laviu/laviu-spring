package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class SanctionRequestDTO {
    private Integer sanctionedUserId;
    private String type; // chatBan, kick
}
