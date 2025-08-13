package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersResponse.StreamerDTO getStreamerDetailDto(Integer userId, Integer tokenUserId) {
        Users other = usersRepository.detailUsersInfoFindByIdAndIsFollow(userId, tokenUserId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_USER));


        UsersResponse.StreamerDTO usersDto =
                new UsersResponse.StreamerDTO(other);

    }

    public UsersResponse.MeDTO getMyDetailDto(Integer userId) {
    }
}
