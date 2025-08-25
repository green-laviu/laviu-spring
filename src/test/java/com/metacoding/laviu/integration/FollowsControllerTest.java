package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu._core.utils.JwtUtil;
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


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class FollowsControllerTest extends MyRestDoc {


    @Autowired
    private ObjectMapper om;
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
    public void save_test() throws Exception {

        //given
        Integer followerId = 2;
        Integer followingId = 3;

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/follows/user/{followerId}/following/{followingId}", followerId, followingId)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followerId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followingId").value(3));
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
                        .header("Authorization", accessToken)
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
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followerId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followingId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isNotificationsEnabled").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void notify_off_test() throws Exception {

        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/follows/{followId}/notify-off", followId)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followerId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.followingId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.isNotificationsEnabled").value(false));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void follow_list_test() throws Exception {
        //given

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/follows/")
                        .header("Authorization", accessToken)

        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user.profileImageUrl")
                .value("https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user.bio").value("안녕하세요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].isLive").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);


    }

    @Test
    public void live_list_test() throws Exception {
        //given
        Integer followId = 1;
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/follows/live")
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamKey").value("cfy_aDktqoqESx6g1DGBEw=="));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.profileImageUrl")
                .value("https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.bio").value("안녕하세요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].viewerCount").value(100));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].thumbnailUrl").value("https://cdn.inflearn.com/wp-content/uploads/javavavava.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value("LIVE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hashtagList[0].hashtagId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hashtagList[0].hashtagName").value("게임"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}
