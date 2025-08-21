package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class UsersControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private String accessToken;

    /**
     * streamer =1번user로 설정되었습니다.
     */
    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        Users ssar = Users.builder()
                .id(1)
                .nickname("ssar")
                .email("ssar@nate.com")
                .roles("USER")
                .build();
        accessToken = JwtUtil.create(ssar);
    }

    @Test
    public void get_users_test() throws Exception {
        // given
        int userId = 3; // user= love의 정보 보기

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/users/" + userId)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

// Streamer 객체 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.userId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.nickname").value("love"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.profileImageUrl").value("https://nate.com/profile3.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.followerCount").value(0));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.bio").value("안녕하세요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.isNotified").value(Matchers.nullValue()));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.streamStatus").value("LIVE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamer.isLive").value(true));

// LiveStream 객체 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.streamId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.title").value("파이썬 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.viewerCount").value(50));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.thumbnailUrl").value("https://example.com/thumb3.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.status").value("LIVE"));

// hashtagList의 0번째 요소만 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.hashtagList[0].hashtagId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.hashtagList[0].hashtagName").value("방송"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.hashtagList", hasSize(1)));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.liveStream.isLive").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void get_me_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/users/me")
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));

// me 객체 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.profileImageUrl").value("https://nate.com/profile1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.followerCount").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.isLive").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.me.bio").value("안녕하세요"));

// live 객체 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.streamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.title").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.viewerCount").value(100));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.thumbnailUrl").value("https://example.com/thumb1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.status").value("LIVE"));

// hashtagList의 0번째 요소만 검증
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.hashtagList[0].hashtagId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.hashtagList[0].hashtagName").value("게임"));

        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.isLive").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void update_users_test() throws Exception {
        // given
        int userId = 1;

        UsersRequest.updateDTO reqDTO = new UsersRequest.updateDTO();
        reqDTO.setUsername("testUser");
        reqDTO.setChannelDescription("tempChannelDescription");
        reqDTO.setProfileImageUrl("temp/url.png");

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/users/" + userId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)

        );
        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value("testUser"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.profileImageUrl").value("temp/url.png"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.bio").value("tempChannelDescription"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void delete_users_test() throws Exception {
        // given
        int userId = 1;
        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/s/api/v1/users/" + userId)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(Matchers.nullValue()));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}