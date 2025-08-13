package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import com.metacoding.laviu.domain.users.service.FollowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/s/api/v1/")
@RestController
public class FollowsController {

    private final FollowsService followsService;

    //follows save
    //request로 안받고 id로 받아오는 body
    @PostMapping("follows/user/{followerId}/following/{followingId}")
    public ResponseEntity<?> save(@PathVariable("followingId") Integer followingId) {

        //1. 유저정보
        Users user = Users.builder().id(1).build();

        //2.save
        FollowsResponse.SaveDTO resDTO = followsService.save(user, followingId);

        return Resp.ok(resDTO);
    }

    //follows delete
    @DeleteMapping("follows/{followId}")
    public ResponseEntity<?> delete(@PathVariable("followId") Integer id) {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        //2.delete
        FollowsResponse.deleteDTO resDTO = followsService.delete(user, id);
        return Resp.ok(resDTO);

    }


}
