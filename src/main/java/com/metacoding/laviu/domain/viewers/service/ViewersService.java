package com.metacoding.laviu.domain.viewers.service;


import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import com.metacoding.laviu.domain.viewers.dto.ViewersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ViewersService {

    private final ViewersRepository viewersRepository;
    private final StreamsRepository streamsRepository;

    /*
     *  웹 소켓 로직에서 처리하는 시청자 등록
     */
    @Transactional
    public Viewers save(String streamKey, Users user) {
        // 1. 방송 조회
        Streams streamPS = streamsRepository.findByStreamKeyAndLive(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        //1. viewers 테이블 확인
        Optional<Viewers> viewerOP = viewersRepository.findByStreamIdAndUserId(streamPS.getId(), user.getId());

        if (viewerOP.isPresent()) throw new ExceptionApi400(ErrorEnum.ALREADY_PARTICIPATING_IN_STREAM);

        //2. viewers 생성
        Viewers viewer = Viewers.builder()
                .user(user)
                .stream(streamPS)
                .build();
        //save
        return viewersRepository.save(viewer);
    }

    /*
     *  시청자 방송 그만 보기
     */
    @Transactional
    public void delete(Integer viewerId) {
        // 시청자 존재 여부 확인
        Viewers viewerPS =
                viewersRepository.findById(viewerId)
                        .orElseThrow(() -> new ExceptionApi404(ErrorEnum.VIEWER_NOT_FOUND));
        // 방송 존재 여부 확인
        Streams streamsPS =
                streamsRepository.findById(viewerPS.getStream().getId())
                        .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        streamsPS.downViewerCount();
        viewersRepository.delete(viewerPS);
    }

    public List<ViewersResponse.ViewersDetailDTO> getList(String streamKey) {
        // 1. 방송 조회
        Streams streamPS = streamsRepository.findByStreamKeyAndLive(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        // 2. 시청자들 조회
        List<Viewers> viewerList = viewersRepository.findAllByStreamId(streamPS.getId());

        // 3. DTO 응답
        return ViewersResponse.ViewersDetailDTO.fromList(viewerList);
    }

}
