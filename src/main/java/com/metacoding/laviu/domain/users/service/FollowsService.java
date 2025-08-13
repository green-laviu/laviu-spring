package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowsService {

    private final FollowsRepository followsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void save(Users user, int followingId) {

        //스트리밍유저 체크
        usersRepository.findById(followingId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_USER));

        //오류 변경
        //팔로워 중인지 체크

        //Follows follows = followsRepository.findByFollowerIdAndFollowingId(followingId, user.getId())
        //  .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOLLOWING));


        // Follows follows = Follows.builder()
        //        .follower(user)
        //        .following(followingId)
        //        .build();
        //save
        // followsRepository.save(follows);

        //pk following id ture false

        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(followingId, user.getId());
    }
}
