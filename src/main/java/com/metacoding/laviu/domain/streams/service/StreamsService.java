package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.utils.StreamKeyGenerator;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.metacoding.laviu._core.error.ErrorEnum.ALREADY_LIVE_STREAMING;
import static com.metacoding.laviu._core.error.ErrorEnum.INVALID_INVITE_RESPONSE_STATE;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {

        // 1.streams 테이블에 user 아이디로 live 인 row가 있는지 확인 없으면 정상 있으면 예외
        Optional<Streams> streamOP = streamsRepository.findBYuserId(user);
        if (streamOP.isPresent()) throw new ExceptionApi400(ALREADY_LIVE_STREAMING);

        //2. streamkey 생성
        String streamKey = StreamKeyGenerator.generate();

        //3. 스트림 저장(엔티티 반환 후 저장)
        Streams stream = reqDTO.toEntity(user, streamKey);
        streamsRepository.save(stream);

        //4.hashtags save TODO
        //4-1 더미데이터 삭제 요망 (연관된 생성자도 삭제필요)
        List<StreamHashtags> streamHashtags = List.of(
                new StreamHashtags(stream, new Hashtags("소통")),
                new StreamHashtags(stream, new Hashtags("게임"))
        );

        //5. 응답 dto로 반환
        return new StreamsResponse.SaveDTO(stream, streamHashtags);
    }
}

