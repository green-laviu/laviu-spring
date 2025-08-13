package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.hashtags.domain.HashtagsRepository;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtagsRepository;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;
    private final FollowsRepository followsRepository;
    private final StreamHashtagsRepository streamHashtagsRepository;
    private final HashtagsRepository hashtagsRepository;


    public UsersResponse.StreamerDTO getStreamerDetailDto(Integer userId, Integer tokenUserId) {
        StreamsResponse.StreamDTO liveStream = getLiveStream(userId);
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(tokenUserId, userId);
        UsersResponse.StreamerDTO.Streamer streamer =
                new UsersResponse.StreamerDTO.Streamer(
                        getUsers(userId),
                        getFollowerCount(userId),
                        isFollowing, liveStream.getStatus()
                );
        return new UsersResponse.StreamerDTO(streamer, liveStream);
    }

    public UsersResponse.MeDTO getMyDetailDto(Integer userId) {
        StreamsResponse.StreamDTO live = getLiveStream(userId);
        UsersResponse.MeDTO.Me me = new UsersResponse.MeDTO.Me(
                getUsers(userId),
                getFollowerCount(userId),
                isLive(live.getStatus())
        );
        return new UsersResponse.MeDTO(me, live);
    }

    // 진행 중이거나 끝난 방송 리턴
    private StreamsResponse.StreamDTO getLiveStream(Integer userId) {
        Streams streamPS = streamsRepository.findByUserIdAndLive(userId).orElse(
                new Streams(StreamsStatus.ENDED)
        );
        return new StreamsResponse.StreamDTO(streamPS);
    }

    // 유저 조회 또는 Exception
    private Users getUsers(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
    }

    // followerCount 얻기
    private Long getFollowerCount(Integer userId) {
        return followsRepository.countByFollowingId(userId);
    }

    //방송여부 확인
    private Boolean isLive(StreamsStatus status) {
        return StreamsStatus.LIVE.equals(status);
    }
}
