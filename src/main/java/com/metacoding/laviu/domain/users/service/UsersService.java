package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.users.domain.Follows;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final FollowsRepository followsRepository;

    //검색시
    public List<UsersResponse.SearchDTO> getSearchUsers(String query, Users user) {

        //1.쿼리 검색(query로 찾고 List로 내보내기)
        List<Users> streamerList = usersRepository.findAllByQuery(query, user.getId());

        //2.팔로워수 랑 팔로잉 여부 확인 후 List 담기
        List<UsersResponse.SearchDTO> searchResultList = new ArrayList<>();
        for (Users streamer : streamerList) {
            //2-1.팔로워수 구하기
            Long followerCount = followsRepository.countByFollowingId(streamer.getId());

            //2-2.초기값 지정
            Boolean isFollowing;
            UsersResponse.FollowStatusDTO FollowStatus;  //DTO 필드값 :followId , isFollowing

            //2-3. 팔로잉 여부 / followPk 보내기
            Optional<Follows> followPS = followsRepository.findByIdAndUserId(streamer.getId(), user.getId());
            if (followPS.isEmpty()) {
                isFollowing = false;
                FollowStatus = new UsersResponse.FollowStatusDTO(null, isFollowing);
            } else {
                isFollowing = true;
                FollowStatus = new UsersResponse.FollowStatusDTO(followPS.get().getId(), isFollowing);
            }

            //3. dto 생성
            UsersResponse.SearchDTO searchResult = new UsersResponse.SearchDTO(streamer, followerCount, FollowStatus);

            //4. list 에 add
            searchResultList.add(searchResult);
        }

        return searchResultList;

    }

    public Users findById(Integer sanctionedUserId) {
        return usersRepository.findById(sanctionedUserId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
    }
}
