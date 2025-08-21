package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Follows;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowsService {

    private final FollowsRepository followsRepository;
    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;

    @Transactional
    public FollowsResponse.SaveDTO save(Users followerUser, Integer followingId) {

        // 나중에 나 자신도 조회 해야함

        //1. 스트리밍유저 체크
        Users followingUserPS = usersRepository.findById(followingId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));

        //2. 팔로잉 중인지 체크(팔로우, 팔로잉 id로)
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(followingId, followerUser.getId());
        if (isFollowing) {
            throw new ExceptionApi400(ErrorEnum.ALREADY_FOLLOWING);
        }

        //3.엔티티 생성
        Follows follow = Follows.builder()
                .follower(followerUser)
                .following(followingUserPS)
                .build();

        //4. save
        Follows followPS = followsRepository.save(follow);

        return new FollowsResponse.SaveDTO(followPS);
    }


    @Transactional
    public void delete(Users user, Integer followId) {

        //1. 조회
        Follows followPS = followsRepository.findById(followId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));

        //2. 권한 체크
        if (!followPS.getFollower().getId().equals(user.getId())) {
            throw new ExceptionApi403(ErrorEnum.NOT_THE_OWNER_OF_FOLLOWING);
        }

        //3.삭제
        followsRepository.delete(followPS);
    }

    @Transactional
    public FollowsResponse.UpdateDTO notifyOn(Users user, Integer followId) {
        //1. 조회
        Follows followPS = followsRepository.findById(followId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));

        // 2. 권한 체크
        if (!followPS.getFollower().getId().equals(user.getId())) {
            throw new ExceptionApi403(ErrorEnum.NOT_THE_OWNER_OF_FOLLOWING);
        }

        followPS.enableNotifications();

        return new FollowsResponse.UpdateDTO(followPS);
    }

    @Transactional
    public FollowsResponse.UpdateDTO notifyOff(Users user, Integer followId) {
        //1. 조회
        Follows followPS = followsRepository.findById(followId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));

        // 2. 권한 체크
        if (!followPS.getFollower().getId().equals(user.getId())) {
            throw new ExceptionApi403(ErrorEnum.NOT_THE_OWNER_OF_FOLLOWING);
        }

        followPS.disableNotifications();

        return new FollowsResponse.UpdateDTO(followPS);
    }

    // 사용자가 팔로우하고 있는 유저의 방송 목록을 StreamDto List로 주는 외부 연동 로직
    public List<StreamsResponse.StreamDTO> followliveList(Users user) {
        return getStreamDtoList(getFollowLiveStreamsList(user));
    }

    // 사용자가 팔로우하고 있는 유저의 목록을 FollowDto list로 주는 외부 연동 로직
    public List<FollowsResponse.FollowDTO> followList(Users user) {
        return getFollowDtoList(getFollowLiveStreamsList(user));
    }

    //---------------------------- 내부 로직 모음 --------------------------------------
    // 방송 리스트에 해당되는 responseDTO List 반환 내부로직
    private List<StreamsResponse.StreamDTO> getStreamDtoList(List<Streams> followLiveStreamsList) {
        List<StreamsResponse.StreamDTO> result = new ArrayList<>();
        for (Streams s : followLiveStreamsList) {
            result.add(new StreamsResponse.StreamDTO(s));
        }
        return result;
    }

    // 사용자가 팔로우 하고 있는 Follows List 반환 내부 로직
    private List<Follows> getFollowsList(Users user) {
        List<Follows> followsPS = followsRepository.findByFollowerId(user.getId());
        if (followsPS.isEmpty()) return List.of();
        return followsPS;
    }

    // 사용자가 팔로우 중인 유저의 Streams List 반환하는 내부 로직
    private List<Streams> getFollowLiveStreamsList(Users user) {
        return streamsRepository.findAllByUserIdsAndStatus(getUserIds(getFollowsList(user)), StreamsStatus.LIVE);
    }

    // 팔로워들의 id만 추출하는 내부 로직
    private Collection<Integer> getUserIds(Collection<Follows> follows) {
        Collection<Integer> result = new HashSet<>();
        for (Follows f : follows) {
            result.add(f.getFollowing().getId());
        }
        return result;
    }

    // 팔로우 중인 방송을 dto로 반환해주는 내부 로직
    private FollowsResponse.FollowDTO getFollowDtoIsFollowingStream(Streams streams) {
        List<FollowsResponse.FollowDTO> result = new ArrayList<>();
        Boolean isLive = streams.getStatus() == StreamsStatus.LIVE;
        return new FollowsResponse.FollowDTO(streams.getStreamer(), true, isLive);
    }

    // 팔로우 중인 방송 목록을 dto 리스트로 반환해 주는 내부 로직
    private List<FollowsResponse.FollowDTO> getFollowDtoList(List<Streams> streams) {
        List<FollowsResponse.FollowDTO> result = new ArrayList<>();
        for (Streams s : streams) {
            result.add(getFollowDtoIsFollowingStream(s));
        }
        return result;
    }
}
