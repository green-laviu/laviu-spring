package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu.domain.streams.service.StreamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rtmp")
public class RtmpController {

    private final StreamsService streamsService;

    // 방송 가능 여부 확인(Streamkey와 Jwt 확인)
    @PostMapping("/on-publish")
    public ResponseEntity<String> onPublish(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(streamsService.verify(params));
    }
}
