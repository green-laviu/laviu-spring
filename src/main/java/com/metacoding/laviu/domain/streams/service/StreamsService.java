package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.utils.CommonUtils;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
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
import com.metacoding.laviu.domain.viewers.dto.ViewersResponse;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
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
    private final ChatMessagesRepository chatMessagesRepository;
    private final ViewersService viewersService;

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {

        // 1.streams 테이블에 user 아이디로 live 인 row가 있는지 확인 없으면 정상 있으면 예외
        Optional<Streams> streamOP = streamsRepository.findByuserId(user.getId());
        if (streamOP.isPresent()) throw new ExceptionApi400(ALREADY_LIVE_STREAMING);

        //2. streamkey 생성
        String streamKey = CommonUtils.generateStreamKey();

        //3. 스트림 저장(엔티티 반환 후 저장)
        Streams stream = reqDTO.toEntity(user, streamKey);
        streamsRepository.save(stream);

        //4.hashtags save TODO
        //4-1 더미데이터 삭제 요망
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
    public void getLiveStreamDetails(int streamId, Users user) {
        //0. 제재상태면 못봄 - 강퇴

        // 1.streams 테이블 조회 및 인증 체크 (STREAMID면서 LIVE인게 있는지 확인)
        Streams stream = streamsRepository.findByIdJoinUser(streamId)
                .orElseThrow(() -> new ExceptionApi400(NO_LIVE_STREAMING));

        //2.viewer save
        viewersService.save(stream, user);

        //3. 스트림 테이블 뷰업테이트 (+1씩 올라가는 함수)
        stream.updateViewerCount();

        //4.팔로워수 팔로워 여부 , 채널주 정보 생성
        Long followerCount = followsRepository.countByFollowingId(stream.getStreamer().getId());
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(stream.getStreamer().getId(), user.getId());
        UsersResponse.ChannelInfoDTO channel = new UsersResponse.ChannelInfoDTO(stream.getStreamer(), followerCount, isFollowing);

        //5.채팅 테이블 목록
        List<ChatMessages> chatList = chatMessagesRepository.findAllByStreamIdJoinUser(stream.getId());
        List<ChatMessagesResponse.ChatDetailDTO> chatResultList = ChatMessagesResponse.ChatDetailDTO.fromList(chatList);

        //6.hlsUrl
        String hlsUrl = "http://host/hls/" + stream.getStreamKey() + ".m3u8";

        //7.HASHTAGE  TODO(삭제/임의데이터) - 로직 변경
        List<StreamHashtags> streamHashtagList = List.of(
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
        List<Viewers> viewerList = viewersRepository.findAllByStreamId(stream.getId());
        List<ViewersResponse.ViewersDetailDTO> viewerResultList = ViewersResponse.ViewersDetailDTO.fromList(viewerList);

        //9.라이브정보 합치기
        LiveDetailDTO live = new LiveDetailDTO(stream, channel, hlsUrl, streamHashtagList);

        //전체 maindetaildto에 담기 (라이브정보 +채팅정보 + 뷰어리스트)
        StreamsResponse.DetailDTO resDTO = new StreamsResponse.DetailDTO(chatResultList, live, viewerResultList);

    }
}

