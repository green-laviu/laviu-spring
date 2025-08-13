package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rtmp")
public class RtmpController {

    private final StreamsService streamsService;

    // 방송 가능 여부 확인(StreamKey와 Jwt 확인)
    @PostMapping("/on-publish")
    public ResponseEntity<?> onPublish(StreamsRequest.StreamsVerifyDTO reqDTO) {
        streamsService.verify(reqDTO);
        return Resp.ok(null);
    }

    @PutMapping("/{streamKey}/thumbnails")
    public ResponseEntity<?> changeThumbnails(@PathVariable String streamKey, @RequestBody StreamsRequest.ThumbnailUpdateDTO reqDTO) {
        streamsService.updateThumbnail(streamKey, reqDTO);
        return Resp.ok(null);
    }
}
