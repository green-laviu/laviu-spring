package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.users.domain.Follows;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowsService {

    private final FollowsRepository followsRepository;
    private final UsersRepository usersRepository;

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

        followPS.enableNotifications();

        return new FollowsResponse.UpdateDTO(followPS);
    }

    @Transactional
    public FollowsResponse.UpdateDTO notifyOff(Users user, Integer followId) {
        //1. 조회
        Follows followPS = followsRepository.findById(followId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));

        followPS.disableNotifications();

        return new FollowsResponse.UpdateDTO(followPS);
    }
}
