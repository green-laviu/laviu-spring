package com.metacoding.laviu.domain.abusereports.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsResponse;
import com.metacoding.laviu.domain.abusereports.service.AbuseReportsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1")
public class AbuseReportsController {

    private final AbuseReportsService abuseReportsService;

    //신고 save
    @PostMapping("/streams/{streamId}/abusereports")
    public ResponseEntity<?> save(@RequestBody AbuseReportsRequest.saveDTO reqDTO, @PathVariable Integer streamId) {

        //1.유저 인증
        Users viewer = Users.builder().id(2).nickname("testname").build();
        //2. 서비스에 넘기기
        AbuseReportsResponse.saveDTO respDTO = abuseReportsService.save(reqDTO, viewer, streamId);

        //TEST용 응답시 "respDTO" 변경 (test용으로 값을 확인해볼려고 생성했어요)
        return Resp.ok(null);

    }
}
