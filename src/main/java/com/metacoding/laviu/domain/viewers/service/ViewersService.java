package com.metacoding.laviu.domain.viewers.service;


import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ViewersService {

    private final ViewersRepository viewersRepository;
    private final StreamsRepository streamsRepository;

    /*
     *  시청자 저장
     */
    @Transactional
    public void save(Streams stream, Users user) {
        //1. viewers 테이블 확인
        Optional<Viewers> viewerOP = viewersRepository.findByStreamIdAndUserId(stream.getId(), user.getId());

        if (viewerOP.isPresent()) throw new ExceptionApi400(ErrorEnum.ALREADY_PARTICIPATING_IN_STREAM);

        //2. viewers 생성
        Viewers viewer = Viewers.builder()
                .user(user)
                .stream(stream)
                .build();
        //save
        Viewers viewersPS = viewersRepository.save(viewer);
    }

    /*
     *  시청자 방송 그민 보기
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

}
