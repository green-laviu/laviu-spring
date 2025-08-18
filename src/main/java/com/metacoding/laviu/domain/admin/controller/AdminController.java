package com.metacoding.laviu.domain.admin.controller;

import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 로그인 페이지
    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "admin-login";
    }

    // 관리자 로그인
    @PostMapping("/v1/auth/admin/login")
    public String login(@RequestBody AdminRequest.LoginDTO reqDTO, HttpSession session) {
        AdminResponse.LoginDTO admin = adminService.login(reqDTO);
        session.setAttribute("ADMIN", admin);
        return "redirect:/v1/admin/streams";
    }

    // 관리자 로그아웃
    @PostMapping("/s/v1/auth/admin/logout")
    public String logout(HttpSession session) {
        if (session != null) session.invalidate();
        return "redirect:/admin/login";
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
    public String processAbuseReport(@PathVariable("id") Integer id, @RequestBody AdminRequest.ProcessReportDTO reqDTO) {
        adminService.processAbuseReport(id, reqDTO.getStatus());
        return "redirect:/s/api/v1/admin/abusereports";
    }
}
