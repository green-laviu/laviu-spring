package com.metacoding.laviu.domain.abusereports.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportCategorys;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsRepository;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsStatus;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AbuseReportsService {

    private final StreamsRepository streamsRepository;
    private final AbuseReportsRepository abuseReportsRepository;

    @Transactional
    public AbuseReportsResponse.saveDTO save(AbuseReportsRequest.saveDTO reqDTO, Users viewer, Integer streamId) {

        //1.방송 스트리밍 검증 (존재하는지)
        Streams streamPS = streamsRepository.findById(streamId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        //2. LIVE 방송 인지 확인
        if (streamPS.getStatus() != StreamsStatus.LIVE) {
            throw new ExceptionApi400(ErrorEnum.STREAM_NOT_LIVE);
        }
        //3.카테고리 검증 (존재하는지)
        AbuseReportCategorys AbuseReportcategoryPS = abuseReportsRepository.findByCategoryIdId(reqDTO.getCategoryId())
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.ABUSEREPORTCATEGORYS_NOT_FOUND));

        //3.엔티티 생성
        AbuseReports abuseReport = AbuseReports.builder()
                .snapshotStreamerNickname(streamPS.getStreamer().getNickname())
                .snapshotStreamTitle(streamPS.getTitle())
                .details(reqDTO.getDetails())
                .category(AbuseReportcategoryPS) // ID만 세팅
                .abuseReportedStream(streamPS)
                .abuseReportedStreamer(streamPS.getStreamer())// 스트림 ID만 세팅
                .abuseReporter(viewer)
                .status(AbuseReportsStatus.PENDING)
                .build();

        //4.save
        AbuseReports abuseReportPS = abuseReportsRepository.save(abuseReport);

        AbuseReportsResponse.saveDTO respDTO = new AbuseReportsResponse.saveDTO(abuseReportPS);
        return respDTO;

    }
}
