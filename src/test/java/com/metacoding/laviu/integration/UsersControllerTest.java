package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class UsersControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void get_users_test() throws Exception {
        // given
        int userId = 1;
        // RTMP 서버의 on-publish 이벤트 바디 형식을 그대로 사용
        // application/x-www-form-urlencoded 형식으로 문자열을 직접 생성합니다.

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/users/" + userId)
                // contentType을 application/x-www-form-urlencoded 로 변경
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void get_me_test() throws Exception {
        // given
        // RTMP 서버의 on-publish 이벤트 바디 형식을 그대로 사용
        // application/x-www-form-urlencoded 형식으로 문자열을 직접 생성합니다.

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/users/me")
                // contentType을 application/x-www-form-urlencoded 로 변경
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }
}