package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable Integer userId, @AuthenticationPrincipal Users principal) {
        UsersResponse.StreamerDTO respDTO = usersService.getStreamerDetail(userId, principal.getId());
        return Resp.ok(respDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal Users principal) {
        UsersResponse.MeDTO respDTO = usersService.getMyDetail(principal.getId());
        return Resp.ok(respDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUsers(@PathVariable Integer userId, @RequestBody UsersRequest.updateDTO updateDTO, @AuthenticationPrincipal Users principal) {
        // TODO 업데이트에는 유저의 정보만 응답 한다. 방송 정보 필요없다
        UsersResponse.UpdateDTO respDTO = usersService.update(updateDTO, userId, principal.getId());
        return Resp.ok(respDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUsers(@PathVariable Integer userId, @AuthenticationPrincipal Users principal) {
        usersService.delete(userId, principal.getId());
        return Resp.ok(null);
    }

}
