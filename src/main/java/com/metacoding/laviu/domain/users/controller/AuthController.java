package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // oauth로그인
    @PostMapping("/oauth/login")
    public ResponseEntity<?> naverOauthLogin(@RequestBody @Valid UsersRequest.LoginDTO reqDTO) {
        var respDTO = authService.naverOauthLogin(reqDTO.getAccessToken());
        return Resp.ok(respDTO);
    }
}
