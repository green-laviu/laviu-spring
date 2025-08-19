package com.metacoding.laviu.domain.viewers.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.dto.SanctionRequestDTO;
import com.metacoding.laviu.domain.chatmessages.dto.SanctionResponseDTO;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewerSanctionsService {
    private final ViewerSanctionsRepository viewerSanctionsRepository;
    private final StreamsRepository streamsRepository;

    public SanctionResponseDTO create(String streamKey, Users user, SanctionRequestDTO reqDTO) {
        Streams streamsPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        return getDto();
    }

    private SanctionResponseDTO getDto() {
    }
}
