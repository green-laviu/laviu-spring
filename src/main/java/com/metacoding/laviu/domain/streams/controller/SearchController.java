package com.metacoding.laviu.domain.streams.controller;


import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/search")
public class SearchController {

    private final UsersService usersService;
    private final StreamsService streamsService;

    //유저검색
    //빈문자열 검색시 -> 값이 없음
    @GetMapping("/users")
    public ResponseEntity<?> getSearchUsers(@Valid UsersRequest.SearchDTO reqDTO) {

        //1. 유저확인( 팔로잉 확인 용으로 사용예정)
        Users user = Users.builder().id(2).build();
        //2.get
        List<UsersResponse.SearchDTO> respDTO = usersService.getSearchUsers(reqDTO.getQuery(), user);

        return Resp.ok(respDTO);
    }

    //스트림 검색
    //빈문자열 검색시 -> 값이 없음
    @GetMapping("/streams")
    public ResponseEntity<?> getSearchStreams(@Valid StreamsRequest.SearchDTO reqDTO) {


        //1. 유저확인 (라이브방송 검색하는데 유저 검증이 필요한가요? )
        Users user = Users.builder().id(2).build();

        //2.get
        List<StreamsResponse.StreamDTO> respDTO = streamsService.getSearchStreams(reqDTO.getQuery());

        return Resp.ok(respDTO);
    }


}
