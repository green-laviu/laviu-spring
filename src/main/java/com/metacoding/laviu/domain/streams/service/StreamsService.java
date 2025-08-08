package com.metacoding.laviu.domain.streams.service;

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
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {
        // streams 테이블에 user 아이디로 live 인 row가 있는지 확인 없으면 정상 있으면 예외
        Streams streamConfig;

        //1. streamkey 생성
        String streamKey = UUID.randomUUID().toString();

        //미리 변수 초기화 /try 안에서는 인식이 안되서 return 값에 적기위해
        //Streams stream;
        //throw new ExceptionApi400(ErrorEnum.INTERNAL_SERVER_ERROR);

        //2-1. stream으로 변환 (빌더)
        Streams stream = reqDTO.toEntity(user, streamKey);


        //2-2 save ,제약조건확인
        Streams streamPS = streamsRepository.save(stream);

        //3.hashtags save TODO
        //3-1 더미데이터 삭제 요망 (연관된 생성자도 삭제필요)
        List<StreamHashtags> streamHashtags = List.of(
                new StreamHashtags(streamPS, new Hashtags("소통")),
                new StreamHashtags(streamPS, new Hashtags("게임"))
        );
        //4. 응답 dto로 반환
        return new StreamsResponse.SaveDTO(streamPS, streamHashtags);
    }
}

