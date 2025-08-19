package com.metacoding.laviu.domain.abusereports.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsResponse;
import com.metacoding.laviu.domain.abusereports.service.AbuseReportsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1")
public class AbuseReportsController {

    private final AbuseReportsService abuseReportsService;

    //신고 save
    @PostMapping("/streams/{streamId}/abusereports")
    public ResponseEntity<?> save(@RequestBody AbuseReportsRequest.saveDTO reqDTO, @PathVariable Integer streamId, @AuthenticationPrincipal Users principal) {

        AbuseReportsResponse.saveDTO respDTO = abuseReportsService.save(reqDTO, principal, streamId);

        return Resp.ok(respDTO);
    }
}
