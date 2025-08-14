package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import com.metacoding.laviu.domain.users.service.FollowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/s/api/v1/follows/")
@RestController
public class FollowsController {

    private final FollowsService followsService;

    //follows save
    //request로 안받고 id로 받아오는 body
    @PostMapping("user/{followerId}/following/{followingId}")
    public ResponseEntity<?> save(@PathVariable("followingId") Integer followingId) {

        //1. 유저정보
        Users user = Users.builder().id(3).build();

        //2.save
        FollowsResponse.SaveDTO resDTO = followsService.save(user, followingId);

        return Resp.ok(resDTO);
    }

    //follows delete
    @DeleteMapping("{followId}")
    public ResponseEntity<?> delete(@PathVariable("followId") Integer followId) {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        //2.delete
        followsService.delete(user, followId);
        return Resp.ok(null);

    }

    @PutMapping("{followId}/notify-on")
    public ResponseEntity<?> notifyOn(@PathVariable("followId") Integer followId) {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        //2.save
        FollowsResponse.UpdateDTO resDTO = followsService.notifyOn(user, followId);

        return Resp.ok(resDTO);
    }

    @PutMapping("{followId}/notify-off")
    public ResponseEntity<?> notifyOff(@PathVariable("followId") Integer followId) {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        //2.save
        FollowsResponse.UpdateDTO resDTO = followsService.notifyOff(user, followId);

        return Resp.ok(resDTO);
    }

    @GetMapping
    public ResponseEntity<?> followList() {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        return Resp.ok(null);
    }

    @GetMapping("live")
    public ResponseEntity<?> liveList() {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        return Resp.ok(null);
    }

}
