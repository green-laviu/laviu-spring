package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rtmp")
@Slf4j
public class RtmpController {

    private final StreamsService streamsService;

    // 방송 가능 여부 확인(StreamKey와 Jwt 확인)
    @PostMapping("/on-publish")
    public ResponseEntity<?> onPublish(@Valid StreamsRequest.StreamsVerifyDTO reqDTO) {
        log.debug("방송-검증 요청");
        streamsService.verify(reqDTO);
        log.debug("방송-검증 결과 : {}", "null");
        return Resp.ok(null);
    }

    @PutMapping("/{streamKey}/thumbnails")
    public ResponseEntity<?> changeThumbnails(@PathVariable String streamKey, @Valid @RequestBody StreamsRequest.ThumbnailUpdateDTO reqDTO) {
        log.debug("방송-썸네일저장 요청");
        streamsService.updateThumbnail(streamKey, reqDTO);
        log.debug("방송-썸네일저장 결과 : {}", "null");
        return Resp.ok(null);
    }
}
