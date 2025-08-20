package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.notifications.dto.NotificationsResponse;
import com.metacoding.laviu.domain.notifications.service.NotificationsService;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class NotificationsControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private StreamsRepository streamsRepository;
    @Autowired
    private NotificationsService notificationsService;

    private String accessToken;

    /**
     * viewer =2번user로 설정되었습니다.
     */
    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        Users cos = Users.builder()
                .id(2)
                .nickname("cos")
                .email("cos@nate.com")
                .roles("USER")
                .build();
        accessToken = JwtUtil.create(cos);
    }

    @Test
    public void get_notification_list_test() throws Exception {
        //given

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/notifications")
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].notificationId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.profileImageUrl")
                .value("https://nate.com/profile1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content")
                .value("ssar님의 방송이 시작되었습니다. 시청하러 가볼까요?"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].receivedAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void update_is_read_test() throws Exception {

        //given
        Integer notificationId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/notifications/{notificationId}", notificationId)
                        .header("Authorization", "Bearer " + accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.notificationId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.relatedEntityId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.content")
                .value("ssar님의 방송이 시작되었습니다. 시청하러 가볼까요?"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isRead").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.type").value("LIVE_STARTED"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void save_test() throws Exception {

        Streams streamsPS = streamsRepository.findById(1).orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));
        List<NotificationsResponse.DTO> notificationList = notificationsService.save(streamsPS);

        System.out.println("알림저장 유저ID : " + notificationList.get(0).getUserId());
        System.out.println("알림저장 알림ID : " + notificationList.get(0).getNotificationId());
        System.out.println("알림저장 타입 : " + notificationList.get(0).getType());
        System.out.println("알림저장 READ상태 : " + notificationList.get(0).getIsRead());
        System.out.println("알림저장 내용 : " + notificationList.get(0).getContent());
        System.out.println("알림저장 생성일 : " + notificationList.get(0).getCreatedAt());
        System.out.println("알림저장 방송ID : " + notificationList.get(0).getRelatedEntityId());
    }

}


