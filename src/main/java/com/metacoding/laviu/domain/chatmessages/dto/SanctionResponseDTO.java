package com.metacoding.laviu.domain.chatmessages.dto;

import com.metacoding.laviu._core.utils.CommonUtils;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctions;
import lombok.Data;

@Data
public class SanctionResponseDTO {
    private String sanctionedUserEmailId;
    private String sanctionedUserEmail;
    private String sanctionedUserNickname;
    private String sanctionType; // chatBan, kick
    private Integer offenseCount;
    private Integer sanctionDurationSeconds;

    public SanctionResponseDTO(ViewerSanctions sanction) {
        String email = sanction.getSanctionedUser().getEmail();
        this.sanctionedUserEmailId = CommonUtils.localPart(email);
        this.sanctionedUserEmail = email;
        this.sanctionedUserNickname = sanction.getSanctionedUser().getNickname();
        this.sanctionType = sanction.getType().name();
        this.offenseCount = sanction.getOffenseCount();
        this.sanctionDurationSeconds = (this.offenseCount != null && this.offenseCount >= 1 && this.offenseCount <= 2)
                ? this.offenseCount * 30 // 임의로 30 넣음
                : null;
    }
}
