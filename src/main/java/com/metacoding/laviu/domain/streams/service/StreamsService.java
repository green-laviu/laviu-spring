package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.utils.StreamKeyGenerator;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import com.metacoding.laviu.domain.viewers.dto.ViewersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        //int followerCount = followsRepository.countByFollowing_Id(stream.getStreamer());
        // boolean isFollowing = followsRepository.existsByFollower_IdAndFollowing_Id(stream.getStreamer(), user);
        // UsersResponse.LiveDetailDTO channel = new UsersResponse.LiveDetailDTO(stream.getStreamer(), followerCount, isFollowing);

        //5.채팅 테이블 정보 받기


        //LiveDetailDTO liveDTO = null;
        //채팅 메시지 정보 조회
        // List < ChatMessagesResponse.ChatDetailDTO > chatListDTO = List.of();
        // StreamsResponse.DetailDTO resDTO = new StreamsResponse.DetailDTO(liveDTO, chatListDTO);

    }
}

