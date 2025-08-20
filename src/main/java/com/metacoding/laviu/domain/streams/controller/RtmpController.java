package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rtmp")
@Slf4j
public class RtmpController {

    private final StreamsService streamsService;

    // 방송 가능 여부 확인(StreamKey와 Jwt 확인)
    @PostMapping("/on-publish")
    public ResponseEntity<?> onPublish(HttpServletRequest request, StreamsRequest.StreamsVerifyDTO reqDTO) {
        log.debug("=== HTTP 요청 디버깅 ===");
        log.debug("Content-Type: {}", request.getContentType());
        log.debug("Method: {}", request.getMethod());
        log.debug("Query String: {}", request.getQueryString());

        // 모든 파라미터 출력
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, values) -> {
            log.debug("Parameter [{}]: {}", key, Arrays.toString(values));
        });

        // 모든 헤더 출력
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            log.debug("Header [{}]: {}", headerName, request.getHeader(headerName));
        });

        log.debug("DTO 바인딩 결과: {}", reqDTO);
        log.debug("========================");
        streamsService.verify(reqDTO);
        log.debug("방송-검증 결과 : {}", "null");
        return Resp.ok(null);
    }

    @PutMapping("/{streamKey}/thumbnails")
    public ResponseEntity<?> changeThumbnails(@PathVariable String streamKey, @RequestBody StreamsRequest.ThumbnailUpdateDTO reqDTO) {
        log.debug("방송-썸네일저장 요청");
        streamsService.updateThumbnail(streamKey, reqDTO);
        log.debug("방송-썸네일저장 결과 : {}", "null");
        return Resp.ok(null);
    }
}
