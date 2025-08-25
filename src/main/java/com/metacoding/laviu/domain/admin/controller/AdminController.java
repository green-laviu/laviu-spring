package com.metacoding.laviu.domain.admin.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // 로그인 페이지
    @GetMapping("/v1/admin/login-form")
    public String adminLoginForm() {
        return "admin-login";
    }

    // 관리자 로그인
    @PostMapping("/v1/auth/admin/login")
    public String login(@Valid @RequestBody AdminRequest.LoginDTO reqDTO, Error error, HttpSession session) {
        AdminResponse.LoginDTO admin = adminService.login(reqDTO);
        session.setAttribute("ADMIN", admin);
        return "redirect:/v1/admin/streams";
    }

    // 실시간 방송 관리
    @GetMapping("/s/v1/admin/streams")
    public String adminStreamManage(HttpServletRequest request) {
        AdminResponse.StreamListDTO model = adminService.adminStreamList();
        request.setAttribute("model", model);
        return "admin-stream-manage"; // mustache 파일명
    }

    // 유저 목록
    @GetMapping("/s/v1/admin/users")
    public String adminUserList(HttpServletRequest request) {
        AdminResponse.UserListDTO model = adminService.adminUserList();
        request.setAttribute("model", model);
        return "admin-user-list";
    }

    // 신고 내역
    @GetMapping("/s/v1/admin/abusereports")
    public String adminReportList(HttpServletRequest request) {
        AdminResponse.ReportListDTO model = adminService.adminReportList();
        request.setAttribute("model", model);
        return "admin-report-list";
    }

    // 신고 내역 수락/거절
    @PostMapping("/s/v1/admin/abusereports/{id}")
    public String processAbuseReport(@PathVariable("id") Integer id, @Valid @RequestBody AdminRequest.ProcessReportDTO reqDTO, Error error) {
        adminService.processAbuseReport(id, reqDTO.getStatus());
        return "redirect:/s/api/v1/admin/abusereports";
    }


    // 관리자 권한으로 방송을 강제 종료
    @PutMapping("/s/v1/admin/streams/{streamKey}/end")
    public ResponseEntity<?> adminStreamEnd(@PathVariable(name = "streamKey") String streamKey) {
        log.debug("관리자 방송 종료 요청: streamId={}", streamKey);
        adminService.adminStreamEnd(streamKey);
        log.debug("관리자 방송 종료 성공: streamId={}", streamKey);
        return Resp.ok(null);
    }
}
