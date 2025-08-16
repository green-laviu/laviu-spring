package com.metacoding.laviu.domain.notifications.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.notifications.domain.Notifications;
import com.metacoding.laviu.domain.notifications.domain.NotificationsRepository;
import com.metacoding.laviu.domain.notifications.domain.NotificationsType;
import com.metacoding.laviu.domain.notifications.dto.NotificationsResponse;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Follows;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final StreamsRepository streamsRepository;
    private final FollowsRepository followsRepository;

    @Transactional
    public void save(Streams stream) {

        //follow list 만들기 (알림이 true인것만)
        List<Follows> followList = followsRepository.findAllByIdAndNotify(stream.getId());

        //팔로워들 마다 인서트 실행
        for( Follows follow : followList) {

            Notifications notification = Notifications.builder()
                    .content("님이 라이브를 시작했어요")
                    .relatedEntityId(stream.getId())
                    .user(follow.getFollower())
                    .type(NotificationsType.LIVE_STARTED)
                    .build();

            Notifications notificationPS = notificationsRepository.save(notification);
        }


    }

    //전체 목록조회
    public List< NotificationsResponse.NotificationsListDto> findAll(Users user) {

        //1.list 목록 조회
        List<Notifications> NotificationList = notificationsRepository.findAll(user.getId());

        //2. 미리 인스턴스 생성
        List<NotificationsResponse.NotificationsListDto> respDTO  = new ArrayList<>();

        for (Notifications notification : NotificationList) {
            //스트리머 정보 조회
            Streams streamPS = streamsRepository.findById(notification.getRelatedEntityId())
                    .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

            NotificationsResponse.NotificationsListDto notificationDto = new NotificationsResponse.NotificationsListDto(notification,streamPS);
            respDTO.add(notificationDto);

        }
            return  respDTO;


    }



}
