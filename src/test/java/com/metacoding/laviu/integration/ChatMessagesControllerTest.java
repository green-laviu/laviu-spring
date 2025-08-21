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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class ChatMessagesControllerTest extends MyRestDoc {

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
    public void get_chat_list_test() throws Exception {
        // given
        Integer streamId = 1;

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/streams/{streamId}/chats", streamId)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].authorId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].authorNickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].content").value("오늘 저녁 뭐 드셨어요?"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].streamer").value(true));
    }
}