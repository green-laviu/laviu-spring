package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/users")
public class UsersController {

    private final UsersService usersService;
    private Integer tokenUserId = 1;    // 토큰에서 조회 예정

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable Integer userId) {
        UsersResponse.StreamerDTO users = usersService.getStreamerDetailDto(userId, tokenUserId);
        return Resp.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        UsersResponse.MeDTO users = usersService.getMyDetailDto(tokenUserId);
        return Resp.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUsers(@PathVariable Integer userId, @RequestBody UsersRequest.updateDTO updateDTO) {
        usersService.update(updateDTO, userId, tokenUserId);
        return Resp.ok(null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUsers(@PathVariable Integer userId) {
        usersService.delete(userId, tokenUserId);
        return Resp.ok(null);
    }

}
