package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu._core.utils.CommonUtils;
import com.metacoding.laviu._core.utils.StringTrimUtils;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.hashtags.service.HashtagsService;
import com.metacoding.laviu.domain.notifications.service.NotificationsService;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.LiveDetailDTO;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctions;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsRepository;
import com.metacoding.laviu.domain.viewers.domain.ViewerSanctionsType;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class StreamsService {
    private final StreamsRepository streamsRepository;
    private final FollowsRepository followsRepository;
    private final ViewersService viewersService;
    private final UsersRepository usersRepository;
    private final HashtagsService hashtagsService;
    private final NotificationsService notificationsService;
    private final ChatMessagesRepository chatMessagesRepository;
    private final ViewerSanctionsRepository viewerSanctionsRepository;

    @Transactional
    public void verify(StreamsRequest.StreamsVerifyDTO reqDTO) {
        String streamKey = reqDTO.getName();
        log.debug("streamKey: {}", streamKey);
//        String token = reqDTO.getToken();
//        log.debug("token: {}", token);
//        Users user = JwtUtil.verify(token);

        // 키와 토큰 조회
//        if (token == null) throw new ExceptionApi400(ErrorEnum.TOKEN_IS_MISSING);
        if (streamKey == null) throw new ExceptionApi400(ErrorEnum.STREAM_KEY_IS_MISSING);

        // 파싱
        Integer[] data = CommonUtils.parseStreamKey(streamKey);
        Integer userId = data[0];
        Integer streamId = data[1];

        // Entity 확인
        usersRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
        Streams streamsPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        // 유저 정보와 조회
        if (!streamsPS.getStreamer().getId().equals(userId))
            throw new ExceptionApi403(ErrorEnum.NOT_THE_STREAMER_OF_THIS_STREAM);

        // 연결이 끊어졌다가 다시 스트림 하면 아래의 조건이 실행됨
        if (streamsPS.getStatus() == StreamsStatus.LIVE) return;

        streamsPS.startLive();

        //팔로워에게 알림저장
        notificationsService.save(streamsPS);
    }

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {

        // 1.streams 테이블에 user 아이디로 live 가 존재하는지 확인
        Optional<Streams> streamOP = streamsRepository.findByUserIdAndLive(user.getId());

        // 2. live 데이터가 존재하면 예외
        if (streamOP.isPresent()) throw new ExceptionApi400(ErrorEnum.STREAM_IS_ALREADY_LIVE);

        // 3. 스트림 저장(엔티티 반환 후 저장)
        Streams stream = reqDTO.toEntity(user);
        Streams streamPS = streamsRepository.save(stream);

        // 4. streamKey 생성
        String streamKey = CommonUtils.generateStreamKey(user.getId(), streamPS.getId());

        streamPS.setStreamKey(streamKey);

        // 5. 해시태그 저장
        // 5-1 해시태그에서 앞뒤 공백 제거 및 내부 공백 1개로 변경
        List<String> normalizedHashtags = Optional.ofNullable(reqDTO.getHashtagList())
                .orElseGet(List::of)
                .stream()
                .map(StringTrimUtils::normalizeSpaces) // 앞뒤 공백 제거 -> utils로 빼놨음
                .filter(tag -> tag != null && !tag.isEmpty()) // 빈값 제거
                .distinct()                                   // 중복 제거
                .toList();

        // 5-2 해시태그 저장/조회 + 매핑 저장
        List<StreamHashtags> streamHashtags = new ArrayList<>(); // 응답/DTO에 내려줄 매핑 목록 버퍼
        for (String hashtagName : normalizedHashtags) { // 정규화·중복제거된 태그명 순회
            Hashtags hashtagPS = hashtagsService.save(hashtagName);
            StreamHashtags sh = StreamHashtags.builder() // 스트림-해시태그 매핑 엔티티 생성
                    .stream(streamPS)   // 현재 저장한 스트림
                    .hashtag(hashtagPS) // 조회/신규 생성된 해시태그
                    .build();
            streamHashtags.add(sh); // 응답용 리스트에 추가
        }
        streamPS.getStreamHashtagList().addAll(streamHashtags);

        return new StreamsResponse.SaveDTO(streamPS);
    }


    /**
     * 방송 보기 (시청자)
     */
    @Transactional
    public StreamsResponse.DetailDTO getLiveStreamDetails(Integer streamId, Users user) {
        //0. 제재상태면 못봄 - 강퇴 TODO
        Optional<ViewerSanctions> viewerSanctionOP = viewerSanctionsRepository.findByStreamIdAndSanctionedUserIdAndTypeAndIsActive(streamId, user.getId(), ViewerSanctionsType.KICK, true);

        if (viewerSanctionOP.isPresent()) {
            throw new ExceptionApi403(ErrorEnum.STREAM_VIEWING_FORBIDDEN);
        }

        // 1.streams 테이블 조회 및 인증 체크 (STREAMID면서 LIVE인게 있는지 확인)
        Streams streamPS = streamsRepository.findByIdJoinStreamer(streamId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        //2.팔로워수 팔로워 여부 , 채널dto 생성
        Long followerCount = followsRepository.countByFollowingId(streamPS.getStreamer().getId());
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(streamPS.getStreamer().getId(), user.getId());
        UsersResponse.ChannelInfoDTO channel = new UsersResponse.ChannelInfoDTO(streamPS.getStreamer(), followerCount, isFollowing);

        //4.hlsUrl
        String hlsUrl = "/hls/" + streamPS.getStreamKey() + ".m3u8";

        //5.라이브정보 합치기
        LiveDetailDTO live = new LiveDetailDTO(streamPS, channel, hlsUrl);

        //전체 maindetaildto에 담기 (라이브정보 +채팅정보 + 뷰어리스트)
        return new StreamsResponse.DetailDTO(live);
    }


    /**
     * 방송 종료하는 서비스
     */
    @Transactional
    public void end(Integer streamsId, Users user) {
        // 방송 없으면 터짐
        Streams streamsPS = streamsRepository.findById(streamsId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        // 권한 없음
        checkStreamerPermission(streamsPS, user.getId());
        // 이미 종료된 방송
        if (streamsPS.getStatus() == StreamsStatus.ENDED)
            throw new ExceptionApi400(ErrorEnum.STREAM_ALREADY_ENDED);
        // 방송 종료
        streamsPS.off();
    }

    @Transactional
    public void updateThumbnail(String streamKey, StreamsRequest.ThumbnailUpdateDTO reqDTO) {
        Streams streamsPS =
                streamsRepository.findByStreamKey(streamKey)
                        .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        streamsPS.updateThumbnailUrl(
                reqDTO.getThumbnailUrl() + "?date=" + System.currentTimeMillis());
    }

    public StreamsResponse.StreamListDTO findAll() {
        List<Streams> liveStreamsList = streamsRepository.findByStatusOrderByViewerCountDesc(StreamsStatus.LIVE);

        if (liveStreamsList.isEmpty()) return null;
        int liveStreamsListSize = liveStreamsList.size();
        int carouselMaxSize = 3;
        int twinMinSize = Math.min(liveStreamsListSize, carouselMaxSize);

        List<Streams> carouselStreamsList = liveStreamsList.subList(0, twinMinSize);
        List<StreamsResponse.StreamDTO> carouselList = new ArrayList<>();
        for (Streams stream : carouselStreamsList) {
            carouselList.add(new StreamsResponse.StreamDTO(stream));
        }

        List<StreamsResponse.StreamDTO> recommendedList = new ArrayList<>();
        if (twinMinSize != liveStreamsListSize) {
            List<Streams> recommendedStreamsList = liveStreamsList.subList(carouselMaxSize, liveStreamsListSize);

            for (Streams stream : recommendedStreamsList) {
                recommendedList.add(new StreamsResponse.StreamDTO(stream));
            }
        } else {
            recommendedList = carouselList;
        }

        return new StreamsResponse.StreamListDTO(carouselList, recommendedList);
    }

    @Transactional
    public StreamsResponse.UpdateDTO update(Integer streamId, Users user, StreamsRequest.UpdateDTO reqDTO) {
        // 유저 조회

        // 방송 조회
        Streams streamPS = streamsRepository.findById(streamId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        // 수정 권한 체크
        checkStreamerPermission(streamPS, user.getId());

        // 수정
        // 해시태그 저장
        // 해시태그에서 앞뒤 공백 제거 및 내부 공백 1개로 변경
        List<String> normalizedHashtags = Optional.ofNullable(reqDTO.getHashtagList())
                .orElseGet(List::of)
                .stream()
                .map(StringTrimUtils::normalizeSpaces) // 앞뒤 공백 제거 -> utils로 빼놨음
                .filter(tag -> tag != null && !tag.isEmpty()) // 빈값 제거
                .distinct()                                   // 중복 제거
                .toList();

        // 해시태그 저장/조회 + 매핑 저장
        List<StreamHashtags> streamHashtagList = new ArrayList<>(); // 응답/DTO에 내려줄 매핑 목록 버퍼
        for (String hashtagName : normalizedHashtags) { // 정규화·중복제거된 태그명 순회
            Hashtags hashtagPS = hashtagsService.save(hashtagName);

            StreamHashtags sh = StreamHashtags.builder() // 스트림-해시태그 매핑 엔티티 생성
                    .stream(streamPS)   // 현재 저장한 스트림
                    .hashtag(hashtagPS) // 조회/신규 생성된 해시태그
                    .build();
            streamHashtagList.add(sh); // 응답용 리스트에 추가
        }

        // 타이틀 변경
        streamPS.updateInfo(reqDTO.getTitle(), streamHashtagList);

        // 응답
        return new StreamsResponse.UpdateDTO(streamPS);
    }

    private void checkStreamerPermission(Streams streams, Integer streamerId) {
        if (!streams.getStreamer().getId().equals(streamerId))
            throw new ExceptionApi403(ErrorEnum.NOT_THE_STREAMER_OF_THIS_STREAM);
    }

    //스트림 검색
    public List<StreamsResponse.StreamDTO> getSearchStreams(String query) {

        //1. 구하기
        List<Streams> streamList = streamsRepository.findAllByQuery(query);

        //2. dto로 변환
        List<StreamsResponse.StreamDTO> respDTO = streamList.stream()
                .map(StreamsResponse.StreamDTO::new) // 생성자에 Streams 넣어서 DTO 변환
                .toList();

        return respDTO;
    }

    // 방송 주인 확인
    public Boolean isStreamOwner(String streamKey, Integer userId) {
        return streamsRepository.existsByStreamKeyAndUserId(streamKey, userId);
    }
}
