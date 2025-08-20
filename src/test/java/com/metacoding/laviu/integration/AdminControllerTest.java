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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
public class AdminControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    private MockHttpSession session;

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
        AdminRequest.LoginDTO reqDTO = new AdminRequest.LoginDTO();
        reqDTO.setEmail("admin@nate.com");
        reqDTO.setPassword("1234");
        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                post("/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then - Security filter가 가로채서 실패 처리 → 403
        actions.andExpect(status().isFound())
                .andDo(print())
                .andDo(document);
    }

    @Test
    void login_fail_test() throws Exception {
        // given
        AdminRequest.LoginDTO reqDTO = new AdminRequest.LoginDTO();
        reqDTO.setEmail("wrong_admin@nate.com");
        reqDTO.setPassword("wrong_password");
        String requestBody = om.writeValueAsString(reqDTO);

        // when
        ResultActions actions = mvc.perform(
                post("/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        actions.andExpect(status().isFound())
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
        actions.andExpect(status().isFound())
                .andExpect(header().string("Location", "/v1/admin/login-form"))
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
                post("/s/v1/admin/abusereports/{id}", reportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .session(session)
        );

        // then - 정상적으로 처리되면 redirect
        actions.andExpect(status().isFound())
                .andExpect(header().string("Location", "/s/v1/admin/abusereports"))
                .andDo(print())
                .andDo(document);
    }
}
