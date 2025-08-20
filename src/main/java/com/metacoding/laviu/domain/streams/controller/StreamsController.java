package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/streams")
@Slf4j
public class StreamsController {

    private final StreamsService streamsService;

    //방송시작(준비) stream save
    @PostMapping("/start")
    public ResponseEntity<?> save(@Valid @RequestBody StreamsRequest.SaveDTO reqDTO, Error error, @AuthenticationPrincipal Users principal) {
        log.debug("방송-준비 요청");
        StreamsResponse.SaveDTO respDTO = streamsService.save(reqDTO, principal);
        log.debug("방송-준비 결과 : {}", respDTO.toString());
        return Resp.ok(respDTO);
    }

    //방송보기(datail화면 조회)
    @GetMapping("/{streamId}")
    public ResponseEntity<?> get(@PathVariable Integer streamId, @AuthenticationPrincipal Users principal) {
        log.debug("방송-상세정보 요청");
        StreamsResponse.DetailDTO respDTO = streamsService.getLiveStreamDetails(streamId, principal);
        log.debug("방송-상세정보 결과 : {}", respDTO.toString());
        return Resp.ok(respDTO);
    }


    @PutMapping("/{streamId}/end")
    public ResponseEntity<?> end(@PathVariable Integer streamId, @AuthenticationPrincipal Users principal) {
        log.debug("방송-종료 요청");
        streamsService.end(streamId, principal);
        log.debug("방송-종료 결과: {}", "null");
        return Resp.ok(null);
    }

    @GetMapping
    public ResponseEntity<?> getStreamsList() {
        log.debug("방송-목록 요청");
        StreamsResponse.StreamListDTO respDTO = streamsService.findAll();
        log.debug("방송-목록 결과 : {}", respDTO.toString());
        return Resp.ok(respDTO);
    }

    @PutMapping("/{streamId}/setting")
    public ResponseEntity<?> update(@PathVariable Integer streamId, @Valid @RequestBody StreamsRequest.UpdateDTO reqDTO, Error error, @AuthenticationPrincipal Users principal) {
        log.debug("방송-수정 요청");
        StreamsResponse.UpdateDTO respDTO = streamsService.update(streamId, principal, reqDTO);
        log.debug("방송-수정 결과 : {}", respDTO.toString());
        return Resp.ok(respDTO);
    }
}
