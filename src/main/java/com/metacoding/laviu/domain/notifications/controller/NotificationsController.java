package com.metacoding.laviu.domain.notifications.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.notifications.dto.NotificationsResponse;
import com.metacoding.laviu.domain.notifications.service.NotificationsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/s/api/v1/notifications")
@RestController
public class NotificationsController {

    private final NotificationsService notificationsService;


    @GetMapping
    public ResponseEntity<?> getNotificationList() {

        //1.유저 정보 검색
        Users user = Users.builder().id(2).build();

        List< NotificationsResponse.NotificationsListDto> respDTO = notificationsService.findAll(user);

        return Resp.ok(respDTO);
    }
}


