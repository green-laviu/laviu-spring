package com.metacoding.laviu.domain.notifications.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.notifications.dto.NotificationsResponse;
import com.metacoding.laviu.domain.notifications.service.NotificationsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/s/api/v1/notifications")
@RestController
public class NotificationsController {

    private final NotificationsService notificationsService;


    //알림 수신함 목록
    @GetMapping
    public ResponseEntity<?> getNotificationList() {

        //1.유저 정보 검색
        Users user = Users.builder().id(2).build();

        //list 조회
        List<NotificationsResponse.NotificationsListDto> respDTO = notificationsService.findAll(user);

        return Resp.ok(respDTO);
    }

    //알림클릭시 is read =ture 로 update
    @PutMapping("/{notificationId}")
    ResponseEntity<?> updateIsRead(@PathVariable Integer notificationId) {

        //read로 변경 메소드
        NotificationsResponse.UpdateIsReadDTO respDTO = notificationsService.markAsRead(notificationId);

        return Resp.ok(respDTO);
    }

}


