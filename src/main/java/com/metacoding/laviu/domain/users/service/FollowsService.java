package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
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

        //1. 스트리밍유저 체크
        usersRepository.findById(followingId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));

        //2. 팔로워 중인지 체크(팔로우, 팔로잉 id로)
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(followingId, followerUser.getId());
        if (isFollowing) {
            throw new ExceptionApi400(ErrorEnum.ALREADY_FOLLOWING);
        }

        //3.엔티티 생성
        Follows follow = Follows.builder()
                .follower(followerUser)
                .following(Users.builder().id(followingId).build())
                .build();

        //4. save
        Follows followPS = followsRepository.save(follow);

        return new FollowsResponse.SaveDTO(followPS, true);

    }


    @Transactional
    public FollowsResponse.deleteDTO delete(Users user, Integer id) {

        //1. 팔로워 중인지 체크(pk로)
        Follows follow = followsRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));

        //2.삭제
        followsRepository.delete(follow);
        return new FollowsResponse.deleteDTO(id, false);

    }
}
