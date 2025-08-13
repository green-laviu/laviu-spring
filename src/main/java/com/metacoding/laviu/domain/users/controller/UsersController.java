package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable Integer userId) {

        Integer tokenUserId = 2; // 토큰으로 조회
        UsersResponse.StreamerDTO users = usersService.getStreamerDetailDto(userId, tokenUserId);
        return Resp.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Integer userId = 2; // 토큰으로 조회
        UsersResponse.MeDTO users = usersService.getMyDetailDto(userId);
        return Resp.ok(users);
    }
}
