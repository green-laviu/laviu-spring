package com.metacoding.laviu.domain.abusereports.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsRepository;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
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
    public void save(AbuseReportsRequest.saveDTO reqDTO, Users streamer) {

        //1.방송 스트리밍 중인지 확인
        Streams streamPS = streamsRepository.findById(streamer.getId())
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        //2. LIVE 인지 확인
        if (streamPS.getStatus() != StreamsStatus.LIVE) {
            throw new ExceptionApi400(ErrorEnum.STREAM_NOT_LIVE);
        }

        //2.save
        AbuseReports abuseReports = AbuseReports.builder().build();

    }
}
