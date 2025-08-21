package com.metacoding.laviu.domain.chatmessages.dto;

import lombok.Data;

@Data
public class WsNotificationResponseDTO {
    private String type;
    private SanctionResponseDTO data;

    public WsNotificationResponseDTO(String type, SanctionResponseDTO data) {
        this.type = type;
        this.data = data;
    }
}
