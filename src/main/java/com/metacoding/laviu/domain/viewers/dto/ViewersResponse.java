package com.metacoding.laviu.domain.viewers.dto;

import com.metacoding.laviu._core.utils.CommonUtils;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


public class ViewersResponse {

    @Data
    public static class wsBroadcastDTO {
        private Integer userId;
        private String emailId;           // user mail 에서 @ 앞부분
        private String nickname;       // 작성자 닉네임
        private LocalDateTime connectedAt; //생성일자

        public wsBroadcastDTO(Viewers viewer) {
            this.userId = viewer.getUser().getId();
            this.emailId = CommonUtils.localPart(viewer.getUser().getEmail());
            this.nickname = viewer.getUser().getNickname();
            this.connectedAt = viewer.getConnectedAt();
        }

        public static List<wsBroadcastDTO> fromList(List<Viewers> list) {
            return list.stream().map(wsBroadcastDTO::new).toList();
        }
    }
}
