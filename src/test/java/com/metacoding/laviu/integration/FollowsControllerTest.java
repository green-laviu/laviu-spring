package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class FollowsControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void save_test() throws Exception {
        //given
        Integer followerId = 3;
        Integer followingId = 2;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/follows/user/{followerId}/following/{followingId}", followerId, followingId)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followerId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followingId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isNotificationsEnabled").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void delete_test() throws Exception {

        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/v1/follows/{followId}", followId)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty()); //null or 빈배열
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void notify_on_test() throws Exception {

        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/follows/{followId}/notify-on", followId)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void notify_off_test() throws Exception {

        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/follows/{followId}/notify-off", followId)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void follow_list_test() throws Exception {
        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/follows/")
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void live_list_test() throws Exception {
        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/follows/live")
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }
}
