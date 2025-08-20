package com.metacoding.laviu.domain.abusereports.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsResponse;
import com.metacoding.laviu.domain.abusereports.service.AbuseReportsService;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1")
public class AbuseReportsController {

    private final AbuseReportsService abuseReportsService;

    //신고 save
    @PostMapping("/streams/{streamId}/abusereports")
    public ResponseEntity<?> save(@Valid @RequestBody AbuseReportsRequest.saveDTO reqDTO, Error error, @PathVariable Integer streamId, @AuthenticationPrincipal Users principal) {

        log.debug("{}유저가 {}방송 신고 요청", streamId, principal.getNickname());
        AbuseReportsResponse.saveDTO respDTO = abuseReportsService.save(reqDTO, principal, streamId);
        log.debug("{}유저가 {}방송 신고 완료", streamId, principal.getNickname());
        return Resp.ok(respDTO);
    }
}
