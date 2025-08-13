package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/streams")
public class StreamsController {

    private final StreamsService streamsService;

    //방송시작(준비) stream save
    @PostMapping("/start")
    public ResponseEntity<?> save(@RequestBody StreamsRequest.SaveDTO reqDTO) {

        //1.세션에서  id 꺼내기
        int id = 2;
        Users user = new Users(id);
        //2. UUID로 스트림 키 생성 및 저장
        StreamsResponse.SaveDTO respDTO = streamsService.save(reqDTO, user);
        return Resp.ok(respDTO);

    }

    //방송보기(datail화면 조회)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        //1.뷰어의 id 꺼내기

        Users user = new Users(2);

        //조회
        streamsService.getLiveStreamDetails(id, user);
        return Resp.ok(null);
    }


    @PutMapping("/{streamId}/end")
    public ResponseEntity<?> end(@PathVariable Integer streamId) {
        Integer userId = 2; // token으로 조회 후 사용
        streamsService.delete(streamId, userId);
        return Resp.ok(null);
    }

    @GetMapping
    public ResponseEntity<?> getStreamsList() {
        StreamsResponse.StreamListDTO result = streamsService.findAll();
        return Resp.ok(result);
    }
}
