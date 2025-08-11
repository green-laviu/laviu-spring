package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.utils.StreamKeyGenerator;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.LiveDetailDTO;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import com.metacoding.laviu.domain.viewers.dto.ViewersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.metacoding.laviu._core.error.ErrorEnum.ALREADY_LIVE_STREAMING;
import static com.metacoding.laviu._core.error.ErrorEnum.NO_LIVE_STREAMING;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;
    private final ViewersRepository viewersRepository;
    private final FollowsRepository followsRepository;

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {

        // 1.streams 테이블에 user 아이디로 live 인 row가 있는지 확인 없으면 정상 있으면 예외
        Optional<Streams> streamOP = streamsRepository.findByuserId(user);
        if (streamOP.isPresent()) throw new ExceptionApi400(ALREADY_LIVE_STREAMING);

        //2. streamkey 생성
        String streamKey = StreamKeyGenerator.generate();

        //3. 스트림 저장(엔티티 반환 후 저장)
        Streams stream = reqDTO.toEntity(user, streamKey);
        streamsRepository.save(stream);

        //4.hashtags save TODO
        //4-1 더미데이터 삭제 요망 (연관된 생성자도 삭제필요)
        List<StreamHashtags> streamHashtags = List.of(
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("소통").build())
                        .build(),
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("게임").build())
                        .build()
        );

        //5. 응답 dto로 반환
        return new StreamsResponse.SaveDTO(stream, streamHashtags);
    }

    //livedetail 정보 조회 + 뷰 save + 뷰어 카운트수 업데이트
    @Transactional
    public void getLiveStreamDetails(int id, Users user) {

        // 1.streams 테이블 조회 및 인증 체크 (STREAMID면서 LIVE인게 있는지 확인)
        Streams stream = streamsRepository.findByIdJoinUser(id).orElseThrow(() -> new ExceptionApi400(NO_LIVE_STREAMING));
        //2. 뷰 테이블 인서트 - > 정보 받기
        Viewers viewers = viewersRepository.save(new ViewersRequest.SaveDTO().toEntity(stream, user));

        //3. 스트림 테이블 뷰업테이트
        stream.updateviewerCount();

        //4.팔로워수 팔로워 여부
        Long followerCount = followsRepository.countByFollowingId(stream.getStreamer());
        boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(stream.getStreamer(), user);
        UsersResponse.LiveDetailDTO channel = new UsersResponse.LiveDetailDTO(stream.getStreamer(), followerCount, isFollowing);

        //5.채팅 테이블 정보 받기 TODO
        List<ChatMessagesResponse.ChatDetailDTO> chatListDTO = List.of(
                new ChatMessagesResponse.ChatDetailDTO(1, 1, 1, "오 무야호ㅋㅋㅋ", LocalDateTime.now().minusSeconds(50)),
                new ChatMessagesResponse.ChatDetailDTO(2, 1, 2, "진짜 재밌다", LocalDateTime.now().minusSeconds(30)),
                new ChatMessagesResponse.ChatDetailDTO(3, 1, 3, "와 화질좋다 1080p!", LocalDateTime.now().minusSeconds(10))
        );
        //6.
        String hlsUrl = "111";

        //7.HASHTAGE
        List<StreamHashtags> streamHashtags = List.of(
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("소통").build())
                        .build(),
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("게임").build())
                        .build()
        );

        //8.viewer 목록


        //라이브정보 합치기
         LiveDetailDTO live = new LiveDetailDTO (stream, channel, hlsUrl, streamHashtags);
         //전체 maindto에 담기
         StreamsResponse.DetailDTO resDTO = new StreamsResponse.DetailDTO( chatListDTO,live);

    }
}

