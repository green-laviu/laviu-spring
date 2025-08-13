package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final FollowsRepository followsRepository;

    //검색시
    public List<UsersResponse.ChannelInfoDTO> getSearchUsers(String query, Users user) {

        //1.쿼리 검색(query로 찾고 List로 내보내기)
        List <Users> streamerList = usersRepository.findByQuery(query);

        //팔로워수 랑 팔로잉 여부 확인 후 List 담기
        List<UsersResponse.ChannelInfoDTO> channelList = new ArrayList<>();
        for(Users streamer : streamerList){
            Long followerCount = followsRepository.countByFollowingId(streamer.getId());
            Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(streamer.getId(), user.getId());
            UsersResponse.ChannelInfoDTO channel = new UsersResponse.ChannelInfoDTO(streamer, followerCount, isFollowing);
            channelList.add(channel);
        }

        //검색에서 리스트가 나오고 이 리스트별 버튼으로 팔로잉을 하고 취소하는게 가능하다면
        // true여서 취소가 가능할 경우
        // follows의 pk도 같이 내보내야 취소로 넘어갈 수 있는게 아닌지..?

return channelList;

    }
}
