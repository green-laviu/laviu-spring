package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.domain.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/s/api/v1/")
@RestController
public class FollowsController {

    //팔로우 save
    //request로 안받고id로 받아오는 body
    //User id 생성자
    //보내야 할 값
    @PostMapping("users/{followerId}/followings/{followingId}")
    public ResponseEntity<?> save(@PathVariable("followingId") int followingId) {

        //1. 유저정보
        Users user = new Users(1);

        //


        return Resp.ok(null);
    }

}
