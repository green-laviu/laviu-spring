package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class SanctionResponseDTO {
    private String sanctionedUserNaverId;
    private String sanctionedUserNickname;
    private String type; // chatBan, kick
    private Integer offenseCount;
}
