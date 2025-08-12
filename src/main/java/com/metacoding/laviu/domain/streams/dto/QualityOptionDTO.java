package com.metacoding.laviu.domain.streams.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class QualityOptionDTO {

    private String label;              // 라벨
    private String value;              // 1080p 720P 값
    private boolean isSelected;        // 선택 여부

    //디폴드 값 생성
    private static final List<QualityOptionDTO> DEFAULT_OPTIONS = List.of(
            QualityOptionDTO.builder().label("1080p (최대)").value("1080p").isSelected(true).build(),
            QualityOptionDTO.builder().label("720p 60fps").value("720p60").isSelected(false).build(),
            QualityOptionDTO.builder().label("480p").value("480p").isSelected(false).build(),
            QualityOptionDTO.builder().label("360p").value("360p").isSelected(false).build()
    );

    // 디폴드 값 전부 가져오기
    public static List<QualityOptionDTO> getAllOptions() {
        return DEFAULT_OPTIONS;
    }
}
