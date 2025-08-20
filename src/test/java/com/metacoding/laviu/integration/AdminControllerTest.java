package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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
        // 테스트 시작 전에 관리자 세션을 설정합니다.
        Users admin = Users.builder()
                .id(1)
                .email("admin@nate.com")
                .roles("ADMIN")
                .build();
        AdminResponse.LoginDTO adminUser = new AdminResponse.LoginDTO(admin);
        session = new MockHttpSession();
        session.setAttribute("ADMIN", adminUser);
    }

    /**
     * 로그인 성공
     */
    @Test
    void login_success_test() throws Exception {
        // given
        AdminRequest.LoginDTO req = new AdminRequest.LoginDTO();
        req.setEmail("admin@nate.com");
        req.setPassword("1234");
        String json = om.writeValueAsString(req);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디: " + responseBody);

        // then
        actions.andExpect(status().isFound()); // 리다이렉트 302
    }

    /**
     * 로그인 실패
     */
    @Test
    public void login_fail_test() throws Exception {
        // given
        AdminRequest.LoginDTO req = new AdminRequest.LoginDTO();
        req.setEmail("wrong_admin@nate.com");
        req.setPassword("wrong_password");
        String json = om.writeValueAsString(req);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디: " + responseBody);

        // then
        actions.andExpect(status().isNotFound());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("해당 유저를 찾을 수 없습니다."));
    }

    /**
     * 로그아웃 성공
     */
    @Test
    void logout_success_test() throws Exception {
        // given - 세션이 이미 beforeEach에서 설정됨

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/v1/auth/admin/logout")
                        .session(session) // 세션을 요청에 포함
        );

        // then
        // 리다이렉션이므로 302(Found)를 기대
        actions.andExpect(status().isFound());
        // 리다이렉션 경로 확인
        actions.andExpect(MockMvcResultMatchers.header().string("Location", "/admin/login"));
    }

    /**
     * 실시간 방송 관리 페이지 접근 성공 (관리자)
     */
    @Test
    void admin_stream_manage_test() throws Exception {
        // given - 세션이 이미 beforeEach에서 설정됨

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/v1/admin/streams")
                        .session(session) // 관리자 세션으로 요청
        );

        // then
        actions.andExpect(status().isOk());
    }

    /**
     * 유저 목록 페이지 접근 성공 (관리자)
     */
    @Test
    void admin_user_list_test() throws Exception {
        // given - 세션이 이미 beforeEach에서 설정됨

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/v1/admin/users")
                        .session(session) // 관리자 세션으로 요청
        );

        // then
        actions.andExpect(status().isOk());
    }

    /**
     * 신고 목록 페이지 접근 성공 (관리자)
     */
    @Test
    void admin_report_list_test() throws Exception {
        // given - 세션이 이미 beforeEach에서 설정됨

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.get("/s/api/v1/admin/abusereports")
                        .session(session) // 관리자 세션으로 요청
        );

        // then
        actions.andExpect(status().isOk());
    }

    /**
     * 신고 내역 처리 (수락/거절) 성공
     */
    @Test
    void process_abuse_report_test() throws Exception {
        // given
        // 테스트할 신고 ID 설정
        Integer reportId = 1;

        // 요청 DTO 생성
        AdminRequest.ProcessReportDTO req = new AdminRequest.ProcessReportDTO();
        req.setStatus("REJECTED"); // 혹은 "ACCEPTED"
        String json = om.writeValueAsString(req);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders.post("/s/api/v1/admin/abusereports/{id}", reportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(session) // 관리자 세션으로 요청
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디: " + responseBody);

        // then
        actions.andExpect(status().isFound()); // 리다이렉션이므로 302(Found)를 기대
        actions.andExpect(MockMvcResultMatchers.header().string("Location", "/s/api/v1/admin/abusereports")); // 리다이렉션 경로
    }
}