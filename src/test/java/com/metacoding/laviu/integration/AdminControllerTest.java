package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
public class AdminControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private MockHttpSession session;


    /**
     * 로그인 후 페이지 확인을 위한 인증 유저  = admin
     * 로그인 로직이 되는지 확인하는 유저 = testAdminLogin
     */
    @BeforeEach
    public void setup() {

        // SecurityContext 에 ADMIN 사용자 인증 심어주기
        UserDetails userDetails = User.withUsername("admin@nate.com")
                .password("1234")   // mock 비밀번호
                .roles("ADMIN")     // ROLE_ADMIN
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }


    @Test
    void login_success_test() throws Exception {
        // given
        //testAdminLogin  = 로그인이 되는지 확인을 하는 유저

        // when
        ResultActions actions = mvc.perform(
                post("/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "testAdminLogin@nate.com")
                        .param("password", "1234")
        );

        // then
        actions.andExpect(status().isFound()) // 302 리다이렉트 상태 확인
                .andExpect(redirectedUrl("/s/v1/admin/streams")) // 성공 시 이동 경로 확인
                .andExpect(request().sessionAttribute("SPRING_SECURITY_CONTEXT", notNullValue())) // 시큐리티 세션 저장 확인
                .andExpect(authenticated().withUsername("testAdminLogin@nate.com")) // 로그인 된 사용자 검증
                .andDo(print())
                .andDo(document);
    }

    @Test
    void login_fail_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                post("/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "wrongAdmin@nate.com")
                        .param("password", "12345")
        );

        // then
        actions.andExpect(status().isFound()) // 302 Redirect
                .andExpect(redirectedUrl("/v1/admin/login-form")) // 실패시 리다이렉트 URL
                .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", notNullValue())) // 실패 예외 세션에 저장됨
                .andDo(print())
                .andDo(document);
    }

    @Test
    void logout_success_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                post("/v1/auth/admin/logout")
        );

        // then
        actions.andExpect(status().isFound()) // 302 Redirect 확인
                .andExpect(redirectedUrl("/v1/admin/login-form")) // 로그아웃 후 리다이렉트 경로 확인
                .andExpect(cookie().maxAge("JSESSIONID", 0)) // 세션 쿠키 삭제 확인
                .andDo(print())
                .andDo(document);
    }

    @Test
    void admin_stream_manage_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/s/v1/admin/streams")
                        .session(session)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document);
    }

    @Test
    void admin_user_list_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/s/v1/admin/users")
                        .session(session)
        );

        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document);
    }

    @Test
    void admin_report_list_test() throws Exception {
        // when
        ResultActions actions = mvc.perform(
                get("/s/v1/admin/abusereports")
                        .session(session)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document);
    }

    @Test
    void process_abuse_report_test() throws Exception {
        // given
        Integer reportId = 1;
        AdminRequest.ProcessReportDTO reqDTO = new AdminRequest.ProcessReportDTO();
        reqDTO.setStatus("REJECTED");
        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                post("/s/v1/admin/abusereports/{reportId}", reportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .session(session)
        );

        // then - 정상적으로 처리되면 redirect
        actions.andExpect(status().isFound())
                .andExpect(header().string("Location", "/s/api/v1/admin/abusereports"))
                .andDo(print())
                .andDo(document);
    }
}
