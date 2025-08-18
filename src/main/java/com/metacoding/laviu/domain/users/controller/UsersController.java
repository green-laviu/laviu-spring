package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.domain.Users;
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
        // TODO : 서비스 메서드 이름에 Dto는 제외한다
        UsersResponse.StreamerDTO respDTO = usersService.getStreamerDetailDto(userId, tokenUserId);
        return Resp.ok(respDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        UsersResponse.MeDTO respDTO = usersService.getMyDetailDto(tokenUserId);
        return Resp.ok(respDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUsers(@PathVariable Integer userId, @RequestBody UsersRequest.updateDTO updateDTO) {
        // TODO 업데이트에는 유저의 정보만 응답 한다. 방송 정보 필요없다
        Users users = usersService.update(updateDTO, userId, tokenUserId);
        UsersResponse.MeDTO respDTO = usersService.getMyDetailDto(tokenUserId);
        return Resp.ok(respDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUsers(@PathVariable Integer userId) {
        usersService.delete(userId, tokenUserId);
        return Resp.ok(null);
    }

}
