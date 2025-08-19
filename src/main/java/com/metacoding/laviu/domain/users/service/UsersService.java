package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
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
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;
    private final FollowsRepository followsRepository;

    // 유저 정보 수정
    @Transactional
    public UsersResponse.UpdateDTO update(UsersRequest.updateDTO updateDTO, Integer userId, Integer tokenUserId) {
        Users users = getUsersAndUserPermissionCheck(userId, tokenUserId);
        users.updateProfile(updateDTO.getUsername(), updateDTO.getChannelDescription(), updateDTO.getProfileImageUrl());

        return new UsersResponse.UpdateDTO(users);
    }

    // 유저 회원탈퇴(soft delete)
    @Transactional
    public void delete(Integer userId, Integer tokenUserId) {
        Users users = getUsersAndUserPermissionCheck(userId, tokenUserId);
        usersRepository.delete(users);
    }

    // 다른 유저 정보 조회
    public UsersResponse.StreamerDTO getStreamerDetail(Integer userId, Integer tokenUserId) {
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

    public UsersResponse.MeDTO getMyDetail(Integer userId) {
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
