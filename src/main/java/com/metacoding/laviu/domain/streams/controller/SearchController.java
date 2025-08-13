package com.metacoding.laviu.domain.streams.controller;


import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/search")
public class SearchController {

    private final UsersService usersService;
    private final StreamsService streamsService ;

    //유저검색
    //get- x.www  or post-body 형태인지 확인 필요
    //그냥 처음 로딩시 전체리스트를 위해 query 에 null 이나 "" 시 -> 전체 화면 보이는 로직 추가(required = false,if문)
    @GetMapping("/users")
    public ResponseEntity<?> getSearchUsers(@RequestParam(required = false) String query) {

        //1. 유저확인( 팔로잉 확인 용으로 사용예정)
        Users user = Users.builder().id(2).build();

        //2.get( 공백 제거 후 담기)
        String queryResult = (query == null) ? null : query.trim();
        //3.get 조회
        List< UsersResponse.ChannelInfoDTO> resDTO = usersService.getSearchUsers(queryResult,user);

        return Resp.ok(resDTO);
    }

    //스트림 검색
    //그냥 처음 로딩시 전체리스트를 위해 query 에 null 이나 "" 시 -> 전체 화면 보이는 로직 추가(required = false,if문)
    //해시태그 하나만
    @GetMapping("/streams")
    public ResponseEntity<?> getSearchStreams(@RequestParam(required = false) String query) {


        //1. 유저확인
        Users user = Users.builder().id(2).build();

        //2, 벤 (재재) 확인 TODO


        //2.get( 공백 제거 후 담기)
        String queryResult = (query == null) ? null : query.trim();

        //3.get
        List <StreamsResponse.StreamDTO> resDTO = streamsService.getSearchStreams(queryResult);


        return Resp.ok(resDTO);
    }





}
