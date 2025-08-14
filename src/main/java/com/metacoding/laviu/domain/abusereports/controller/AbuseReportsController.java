package com.metacoding.laviu.domain.abusereports.controller;

import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.abusereports.service.AbuseReportsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1")
public class AbuseReportsController {

    private final AbuseReportsService abuseReportsService;

    //신고 save
    @PostMapping("/streams/{streamId}/abusereports")
    public void save(AbuseReportsRequest.saveDTO reqDTO, @PathVariable Integer streamId) {

        //1.유저 인증
        Users streamer = Users.builder().id(2).build();

        //2. 서비스에 넘기기
        abuseReportsService.save(reqDTO, streamer);

    }
}
