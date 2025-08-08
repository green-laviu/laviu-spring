package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/streams")
public class StreamsController {
    private final StreamsService  streamsService;

    //방송시작(준비) stream save
    @PostMapping("/start")
    public ResponseEntity<?> save(StreamsRequest.SaveDto reqDTO) {

        //1.세션에서  id 꺼내기
        int id =2;

        //2. UUID로 스트림 키 생성 및 저장
        String streamKey = streamsService.save(reqDTO,id);
        return Resp.ok(streamKey);

    }
}
