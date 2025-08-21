package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class SanctionRequestDTO {
    private Integer sanctionedUserId;
    private String sanctionType; // CHAT_BAN, KICK
}
