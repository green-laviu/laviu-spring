// src/test/java/com/metacoding/laviu/integration/AuthControllerTest.java

package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersProvider;
import com.metacoding.laviu.domain.users.dto.UsersRequest;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @MockitoBean // 스프링 컨테이너의 AuthService를 가짜(Mock)로 교체
    private AuthService authService;

    // 시나리오 1: 기존 유저 로그인
    @Test
    public void naver_oauth_login_existing_user_test() throws Exception {
        // given
        Users existingUser = Users.builder()
                .id(1)
                .nickname("existing_user")
                .email("test@example.com")
                .provider(UsersProvider.NAVER)
                .profileImageUrl("profile_image_url_1")
                .build();

        UsersResponse.LoginDTO mockLoginDto = new UsersResponse.LoginDTO(
                existingUser,
                "mock-jwt-token-for-existing-user",
                false
        );

        when(authService.naverOauthLogin(anyString()))
                .thenReturn(mockLoginDto);

        UsersRequest.LoginDTO reqDTO = new UsersRequest.LoginDTO("mock-naver-access-token");
        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                post("/oauth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token-for-existing-user"))
                .andExpect(jsonPath("$.data.isNewUser").value(false))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickname").value("existing_user"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.profileUrl").value("profile_image_url_1"))
                .andExpect(jsonPath("$.data.providerType").value("NAVER"))
                .andDo(document);

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅ 기존 유저 로그인 응답 바디: " + responseBody);
    }

    // 시나리오 2: 신규 유저 로그인
    @Test
    public void naver_oauth_login_new_user_test() throws Exception {
        // given
        Users newUser = Users.builder()
                .id(2)
                .nickname("new_user")
                .email("newuser@example.com")
                .provider(UsersProvider.NAVER)
                .profileImageUrl("profile_image_url_2")
                .build();

        UsersResponse.LoginDTO mockLoginDto = new UsersResponse.LoginDTO(
                newUser,
                "mock-jwt-token-for-new-user",
                true
        );

        when(authService.naverOauthLogin(anyString()))
                .thenReturn(mockLoginDto);

        UsersRequest.LoginDTO reqDTO = new UsersRequest.LoginDTO("mock-naver-access-token-new");
        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                post("/oauth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token-for-new-user"))
                .andExpect(jsonPath("$.data.isNewUser").value(true))
                .andExpect(jsonPath("$.data.userId").value(2))
                .andExpect(jsonPath("$.data.nickname").value("new_user"))
                .andExpect(jsonPath("$.data.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.data.profileUrl").value("profile_image_url_2"))
                .andExpect(jsonPath("$.data.providerType").value("NAVER"))
                .andDo(document);

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅ 신규 유저 로그인 응답 바디: " + responseBody);
    }
}
