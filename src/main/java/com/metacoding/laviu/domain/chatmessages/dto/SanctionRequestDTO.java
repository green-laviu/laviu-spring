package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class SanctionRequestDTO {
    private Integer userId;
    private String type; // chatBan, kick
}
