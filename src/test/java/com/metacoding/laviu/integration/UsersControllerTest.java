package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
        int userId = 4;

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

    @Test
    public void update_users_test() throws Exception {
        // given
        int userId = 2;

        UsersRequest.updateDTO reqDTO = new UsersRequest.updateDTO(
                "testUser",
                "tempChannelDescription",
                "temp/url.png"
        );

        String requestBody = om.writeValueAsString(reqDTO);
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/users/" + userId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                // contentType을 application/x-www-form-urlencoded 로 변경
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void delete_users_test() throws Exception {
        // given
        int userId = 2;
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/v1/users/" + userId)
                // contentType을 application/x-www-form-urlencoded 로 변경
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }
}