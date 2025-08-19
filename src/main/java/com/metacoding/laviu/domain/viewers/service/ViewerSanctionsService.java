package com.metacoding.laviu.domain.viewers.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.dto.SanctionRequestDTO;
import com.metacoding.laviu.domain.chatmessages.dto.SanctionResponseDTO;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctions;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsRepository;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewerSanctionsService {
    private final ViewerSanctionsRepository viewerSanctionsRepository;
    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;

    // 재제를 추가하는 외부 연동 로직
    @Transactional
    public SanctionResponseDTO save(String streamKey, Users streamer, SanctionRequestDTO reqDTO) {
        // 방송 존재 여부 확인
        Streams streamsPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        // 권한 여부 확인
        if (!streamsPS.getStreamer().getId().equals(streamer.getId())) {
            throw new ExceptionApi403(ErrorEnum.ACCESS_IS_DENIED);
        }
        // 기존 재제 존재 유무 확인
        ViewerSanctions sanctionsPS = viewerSanctionsRepository
                .findByStreamIdAndSanctionedUserId(streamsPS.getId(), reqDTO.getSanctionedUserId())
                .orElse(null);
        // 없을 시 새로 생성
        if (sanctionsPS == null) {
            sanctionsPS = create(streamsPS, reqDTO);
        } else {
            // 존재할 시 마지막 제재와 비슷하지만 타입과 offenseCount가 증가된 새로운 재제 인스텐스 추가
            sanctionsPS = copyCreate(sanctionsPS, reqDTO.getType());
        }
        // dto로 return
        return new SanctionResponseDTO(sanctionsPS);
    }

    // 새로운 재제를 만드는 내부 로직
    private ViewerSanctions create(Streams streams, SanctionRequestDTO reqDTO) {
        Users sanctionedUserPS = usersRepository.findById(reqDTO.getSanctionedUserId())
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));

        ViewerSanctionsType type = getSanctionType(reqDTO.getType());
        ViewerSanctions newViewerSanctions = new ViewerSanctions(type, streams, streams.getStreamer(), sanctionedUserPS);
        return viewerSanctionsRepository.save(newViewerSanctions);
    }

    // 기존 재제를 토대로 추가적인 재제 인스텐스를 만드는 내부 로직
    private ViewerSanctions copyCreate(ViewerSanctions sanctions, String type) {
        ViewerSanctionsType sanctionType = getSanctionType(type);
        ViewerSanctions newViewerSanctions = new ViewerSanctions(sanctions);
        newViewerSanctions.update(sanctionType);
        return viewerSanctionsRepository.save(newViewerSanctions);
    }

    // String 타입의 sanctionType을 ViewerSanctionsType으로 변경하는 내부 로직
    private ViewerSanctionsType getSanctionType(String type) {
        try {
            return ViewerSanctionsType.valueOf(type);
        } catch (Exception e) {
            throw new ExceptionApi400(ErrorEnum.BAD_REQUEST_SANCTION_TYPE);
        }
    }
}
