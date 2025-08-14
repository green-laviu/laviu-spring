package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;
    private final FollowsRepository followsRepository;

    // 유저 정보 수정
    @Transactional
    public Users update(UsersRequest.updateDTO updateDTO, Integer userId, Integer tokenUserId) {
        Users users = getUsersAndUserPermissionCheck(userId, tokenUserId);
        users.updataProfile(updateDTO.getUsername(), updateDTO.getChannelDescription(), updateDTO.getProfileImageUrl());

        return users;
    }

    // 유저 회원탈퇴(soft delete)
    @Transactional
    public void delete(Integer userId, Integer tokenUserId) {
        Users users = getUsersAndUserPermissionCheck(userId, tokenUserId);
        usersRepository.delete(users);
    }

    // 다른 유저 정보 조회
    public UsersResponse.StreamerDTO getStreamerDetailDto(Integer userId, Integer tokenUserId) {
        StreamsResponse.UserInfoStreamsDTO liveStream = getLiveStream(userId);
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(tokenUserId, userId);

        UsersResponse.StreamerDTO.Streamer streamer =
                new UsersResponse.StreamerDTO.Streamer(
                        getUsers(userId),
                        getFollowerCount(userId),
                        isFollowing,
                        getStreamsStatus(liveStream)
                );
        return new UsersResponse.StreamerDTO(streamer, liveStream);
    }

    public UsersResponse.MeDTO getMyDetailDto(Integer userId) {
        StreamsResponse.UserInfoStreamsDTO liveStream = getLiveStream(userId);
        UsersResponse.MeDTO.Me me = new UsersResponse.MeDTO.Me(
                getUsers(userId),
                getFollowerCount(userId),
                isLive(getStreamsStatus(liveStream))
        );
        return new UsersResponse.MeDTO(me, liveStream);
    }
    // 진행 중이거나 끝난 방송 리턴

    private StreamsResponse.UserInfoStreamsDTO getLiveStream(Integer userId) {
        Streams streamPS = streamsRepository.findByUserIdAndLive(userId).orElse(null);
        if (streamPS == null) return null;
        return new StreamsResponse.UserInfoStreamsDTO(streamPS);
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

    // 권한 여부 확인
    private Users getUsersAndUserPermissionCheck(Integer userId, Integer tokenUserId) {
        Users users = getUsers(userId);
        if (!users.getId().equals(tokenUserId)) throw new ExceptionApi403(ErrorEnum.ACCESS_IS_DENIED);
        return users;
    }

    // 방송 값에 따른 StreamsStatus 값 추출
    private StreamsStatus getStreamsStatus(StreamsResponse.UserInfoStreamsDTO liveStream) {
        return liveStream == null ? StreamsStatus.ENDED : liveStream.getStatus();
    }
}
