package com.metacoding.laviu.domain.viewers.dto;

import com.metacoding.laviu.domain.viewers.domain.Viewers;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.metacoding.laviu._core.utils.CommonUtils.localPart;


public class ViewersResponse {

    @Data
    public static class ViewersDetailDTO {
        private Integer id;
        private String email;           // user mail 에서 @ 앞부분
        private String nickname;       // 작성자 닉네임
        private LocalDateTime connectedAt; //생성일자

        public static ViewersResponse.ViewersDetailDTO from(Viewers viewer) {
            ViewersResponse.ViewersDetailDTO dto = new ViewersResponse.ViewersDetailDTO();
            dto.id = viewer.getId();
            dto.email = localPart(viewer.getUser().getEmail());
            dto.nickname = viewer.getUser().getNickname();
            dto.connectedAt = viewer.getConnectedAt();
            return dto;
        }

        public static List<ViewersResponse.ViewersDetailDTO> fromList(List<Viewers> list) {
            return list.stream().map(ViewersResponse.ViewersDetailDTO::from).toList(); // JDK16+ (JDK8이면 Collectors.toList())
        }
    }
}
