package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s/api/v1/users")
public class UsersController {
    public ResponseEntity<?> getUsers() {
        return Resp.ok("data");
    }
}
