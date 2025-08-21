package com.metacoding.laviu.domain.users.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.FollowsResponse;
import com.metacoding.laviu.domain.users.service.FollowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> save(@PathVariable("followingId") Integer followingId, @AuthenticationPrincipal Users principal) {
        FollowsResponse.SaveDTO respDTO = followsService.save(principal, followingId);
        return Resp.ok(respDTO);
    }

    //follows delete
    @DeleteMapping("{followId}")
    public ResponseEntity<?> delete(@PathVariable("followId") Integer followId, @AuthenticationPrincipal Users principal) {
        followsService.delete(principal, followId);
        return Resp.ok(null);

    }

    @PutMapping("{followId}/notify-on")
    public ResponseEntity<?> notifyOn(@PathVariable("followId") Integer followId, @AuthenticationPrincipal Users principal) {
        FollowsResponse.UpdateDTO respDTO = followsService.notifyOn(principal, followId);
        return Resp.ok(respDTO);
    }

    @PutMapping("{followId}/notify-off")
    public ResponseEntity<?> notifyOff(@PathVariable("followId") Integer followId, @AuthenticationPrincipal Users principal) {
        FollowsResponse.UpdateDTO respDTO = followsService.notifyOff(principal, followId);
        return Resp.ok(respDTO);
    }

    // 현재 팔로우 하고 있는 유저의 목록 조회
    @GetMapping
    public ResponseEntity<?> followList(@AuthenticationPrincipal Users principal) {
        List<FollowsResponse.FollowDTO> respDTO = followsService.followList(principal);
        return Resp.ok(respDTO);
    }

    // 현재 팔로우 하고있는 유저의 방송 목록 조회
    @GetMapping("live")
    public ResponseEntity<?> liveList(@AuthenticationPrincipal Users principal) {
        List<StreamsResponse.StreamDTO> respDTO = followsService.followliveList(principal);
        return Resp.ok(respDTO);
    }

}
