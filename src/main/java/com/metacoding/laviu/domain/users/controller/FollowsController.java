package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import com.metacoding.laviu.domain.users.service.FollowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        FollowsResponse.SaveDTO respDTO = followsService.save(user, followingId);

        return Resp.ok(respDTO);
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
        FollowsResponse.UpdateDTO respDTO = followsService.notifyOn(user, followId);

        return Resp.ok(respDTO);
    }

    @PutMapping("{followId}/notify-off")
    public ResponseEntity<?> notifyOff(@PathVariable("followId") Integer followId) {

        //1. 유저정보
        Users user = Users.builder().id(2).build();

        //2.save
        FollowsResponse.UpdateDTO respDTO = followsService.notifyOff(user, followId);

        return Resp.ok(respDTO);
    }

    // 현재 팔로우 하고 있는 유저의 목록 조회
    @GetMapping
    public ResponseEntity<?> followList() {

        //1. 유저정보
        Users user = Users.builder().id(2).build();
        List<FollowsResponse.FollowDTO> result = followsService.followDtoList(user);
        return Resp.ok(result);
    }

    // 현재 팔로우 하고있는 유저의 방송 목록 조회
    @GetMapping("live")
    public ResponseEntity<?> liveList() {

        //1. 유저정보
        Users user = Users.builder().id(2).build();
        List<StreamsResponse.StreamDTO> result = followsService.followliveList(user);
        return Resp.ok(result);
    }

}
