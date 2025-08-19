package com.metacoding.laviu.domain.chatmessages.dto;

import com.metacoding.laviu.domain.viewers.domain.ViewerSanctions;
import lombok.Builder;
import lombok.Data;

@Data
public class SanctionResponseDTO {
    private String sanctionedUserNaverId;
    private String sanctionedUserNickname;
    private String type; // chatBan, kick
    private Integer offenseCount;

    @Builder

    public SanctionResponseDTO(ViewerSanctions sanction) {
        String email = sanction.getSanctionedUser().getEmail();
        this.sanctionedUserNaverId = email.substring(0, email.indexOf("@") - 1);
        this.sanctionedUserNickname = sanction.getSanctionedUser().getNickname();
        this.type = sanction.getType().name();
        this.offenseCount = sanction.getOffenseCount();
    }
}
