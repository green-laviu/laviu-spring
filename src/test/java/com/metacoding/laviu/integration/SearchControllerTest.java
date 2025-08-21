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
public class SearchControllerTest extends MyRestDoc {

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
    public void get_search_users_test() throws Exception {
        //given
        String query = "ssar";
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/search/users?query=" + query)
                        .header("Authorization", accessToken)

        );
        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].profileImageUrl")
                .value("https://nate.com/profile1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].followerCount").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].followStatus.followId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].followStatus.isFollowing").value(true));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_search_streams_test() throws Exception {

        //given
        String query = " 게임";
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/search/streams?query=" + query)
                        .header("Authorization", accessToken)

        );
        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.profileImageUrl")
                .value("https://nate.com/profile1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer.bio").value("안녕하세요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].viewerCount").value(100));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].thumbnailUrl").value("https://example.com/thumb1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value("LIVE"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hashtagList[0].hashtagId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hashtagList[0].hashtagName").value("게임"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


}
