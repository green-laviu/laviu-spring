package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtagsRepository;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
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

    public UsersResponse.StreamerDTO getStreamerDetailDto(Integer userId, Integer tokenUserId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
        Streams streamOP = streamsRepository.findByUserIdAndLive(userId).orElse(
                new Streams(StreamsStatus.ENDED)
        );
        Long followerCount = followsRepository.countByFollowingId(userId);

        UsersResponse.StreamerDTO result = new UsersResponse.StreamerDTO(null, null);
        if (result == null) throw new ExceptionApi404(ErrorEnum.USER_NOT_FOUND);
        return result;
    }

    public UsersResponse.MeDTO getMyDetailDto(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
        Streams streamOP = streamsRepository.findByUserIdAndLive(userId).orElse(
                new Streams(StreamsStatus.ENDED)
        );
        Long followerCount = followsRepository.countByFollowingId(userId);
        UsersResponse.MeDTO.Me me = null;
        UsersResponse.MeDTO result = new UsersResponse.MeDTO(null, null);
        return null;
    }
}
