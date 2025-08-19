package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.domain.users.controller.AuthController;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersProvider;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.users.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(MockitoExtension.class) // Mockito 활성화
public class AuthControllerTest {

    private MockMvc mvc;
    private final ObjectMapper om = new ObjectMapper();

    // 테스트할 컨트롤러. Mock 객체들이 이 객체에 주입됩니다.
    @InjectMocks
    private AuthController authController;

    // 컨트롤러가 의존하는 서비스. 이 객체를 Mocking할 것입니다.
    @Mock
    private AuthService authService;

    // LoginDTO 생성자에 사용되므로 Users 엔티티도 Mocking해야 합니다.
    @Mock
    private Users mockUsers;

    @BeforeEach
    public void setup() {
        // MockMvc를 컨트롤러 인스턴스로 수동으로 설정합니다.
        this.mvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void naver_oauth_login_already_user_test() throws Exception {
        // given
        // 요청 JSON 본문을 간단한 문자열로 생성합니다.
        String reqJson = """
                {
                  "accessToken": "naver-access-token-mock",
                  "fcmToken": "mock-fcm-token-1"
                }
                """;

        // LoginDTO에 필요한 값을 제공하기 위해 'Users' 객체의 동작을 Mocking합니다.
        when(mockUsers.getId()).thenReturn(1);
        when(mockUsers.getNickname()).thenReturn("testuser");
        when(mockUsers.getEmail()).thenReturn("ssar@example.com");
        when(mockUsers.getProfileImageUrl()).thenReturn("http://profile.image.url");
        when(mockUsers.getProvider()).thenReturn(UsersProvider.NAVER);

        // 서비스가 반환할 Mock LoginDTO 객체를 생성합니다.
        // 이 생성자에서 Mocking된 Users 객체를 사용합니다.
        UsersResponse.LoginDTO mockLoginDto = new UsersResponse.LoginDTO(
                mockUsers,
                "mock-jwt-token-from-auth-service",
                false // isNewUser
        );

        // AuthService의 동작을 Mocking합니다. naverOauthLogin이 어떤 문자열 인수로 호출되든
        // mockLoginDto를 반환하도록 설정합니다.
        when(authService.naverOauthLogin(anyString()))
                .thenReturn(mockLoginDto);

        // when
        // 컨트롤러에 POST 요청을 보냅니다.
        ResultActions actions = mvc.perform(
                post("/oauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson)
        );

        // then
        // HTTP 상태와 JSON 응답 본문을 검증합니다.
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickname").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("ssar@example.com"))
                .andExpect(jsonPath("$.data.profileUrl").value("http://profile.image.url"))
                .andExpect(jsonPath("$.data.providerType").value("NAVER"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token-from-auth-service"))
                .andExpect(jsonPath("$.data.isNewUser").value(false));

        // 디버깅 목적으로 응답 본문을 로그에 출력합니다.
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        log.info("✅응답 본문: " + responseBody);
    }

    @Test
    public void naver_oauth_login_new_user_test() throws Exception {
        // given
        // 요청 JSON 본문을 간단한 문자열로 생성합니다.
        String reqJson = """
                {
                  "accessToken": "naver-access-token-mock",
                  "fcmToken": "mock-fcm-token-1"
                }
                """;

        // LoginDTO에 필요한 값을 제공하기 위해 'Users' 객체의 동작을 Mocking합니다.
        when(mockUsers.getId()).thenReturn(1);
        when(mockUsers.getNickname()).thenReturn("newuser");
        when(mockUsers.getEmail()).thenReturn("newuser@example.com");
        when(mockUsers.getProfileImageUrl()).thenReturn("http://new.profile.image.url");
        when(mockUsers.getProvider()).thenReturn(UsersProvider.NAVER);
        // 서비스가 반환할 Mock LoginDTO 객체를 생성합니다.
        // 이 생성자에서 Mocking된 Users 객체를 사용하고, isNewUser를 true로 설정합니다.
        UsersResponse.LoginDTO mockLoginDto = new UsersResponse.LoginDTO(
                mockUsers,
                "mock-jwt-token-from-auth-service-new",
                true // isNewUser를 true로 설정
        );

        // AuthService의 동작을 Mocking합니다. naverOauthLogin이 어떤 문자열 인수로 호출되든
        // mockLoginDto를 반환하도록 설정합니다.
        when(authService.naverOauthLogin(anyString()))
                .thenReturn(mockLoginDto);

        // when
        // 컨트롤러에 POST 요청을 보냅니다.
        ResultActions actions = mvc.perform(
                post("/oauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson)
        );

        // then
        // HTTP 상태와 JSON 응답 본문을 검증합니다. isNewUser가 true인지 확인합니다.
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.msg").value("성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickname").value("newuser"))
                .andExpect(jsonPath("$.data.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.data.profileUrl").value("http://new.profile.image.url"))
                .andExpect(jsonPath("$.data.providerType").value("NAVER"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token-from-auth-service-new"))
                .andExpect(jsonPath("$.data.isNewUser").value(true)); // true로 검증

        // 디버깅 목적으로 응답 본문을 로그에 출력합니다.
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        log.info("✅응답 본문: " + responseBody);
    }
}
