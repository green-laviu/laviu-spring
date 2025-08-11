package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.utils.StreamKeyGenerator;
import com.metacoding.laviu._core.utils.StringTrim;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.HashtagsRepository;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtagsRepository;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.metacoding.laviu._core.error.ErrorEnum.ALREADY_LIVE_STREAMING;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;
    private final HashtagsRepository hashtagsRepository;
    private final StreamHashtagsRepository streamHashtagsRepository;

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

        // HashTagsService 에게 reqDTO.hashtags 를 넘겨서
        // 서비스 에서 db 조회 해서 있으면 조회된 엔티티 들을 리스트로 반환, 없으면 insert
        // 받아온 엔티티로 스트림해시태그 db 에 insert 하기
        List<String> normalized = Optional.ofNullable(reqDTO.getHashtags())
                .orElseGet(List::of)
                .stream()
                .map(StringTrim::normalizeSpaces) // 앞뒤 공백 제거 -> utils로 빼놨음
                .filter(tag -> tag != null && !tag.isEmpty()) // 빈값 제거
                .distinct()                                   // 중복 제거
                .toList();

        // 2) 해시태그 저장/조회 + 매핑 저장
        List<StreamHashtags> streamHashtags = new ArrayList<>(); // 응답/DTO에 내려줄 매핑 목록 버퍼
        for (String tagName : normalized) { // 정규화·중복제거된 태그명 순회
            Hashtags hashtag = hashtagsRepository.findByName(tagName) // 태그 존재 여부 조회
                    .orElseGet(() -> { // 없으면 생성로직 수행 (upsert)
                        Hashtags h = Hashtags.builder()
                                .name(tagName) // 태그명 세팅
                                .build();
                        hashtagsRepository.save(h); // 신규 해시태그 영속화
                        return h; // 생성한 엔티티 반환
                    });

            StreamHashtags sh = StreamHashtags.builder() // 스트림-해시태그 매핑 엔티티 생성
                    .stream(stream)   // 현재 저장한 스트림
                    .hashtag(hashtag) // 조회/신규 생성된 해시태그
                    .build();
            streamHashtagsRepository.save(sh); // 매핑 테이블 저장
            streamHashtags.add(sh); // 응답용 리스트에 추가
        }

        return new StreamsResponse.SaveDTO(stream, streamHashtags);
    }
}

