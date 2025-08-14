package com.metacoding.laviu.domain.admin.controller;

import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

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
    @PostMapping("/api/v1/auth/admin/login")
    public String login(AdminRequest.LoginDTO reqDTO, HttpSession session) {
        AdminResponse.LoginDTO admin = adminService.login(reqDTO);
        session.setAttribute("ADMIN", admin);
        return "redirect:/api/v1/admin/streams";
    }

    // 관리자 로그아웃
    @PostMapping("/s/api/v1/auth/admin/logout")
    public String logout(HttpSession session) {
        if (session != null) session.invalidate();
        return "redirect:/admin/login";
    }

    // 실시간 방송 관리
    @GetMapping("/api/v1/admin/streams")
    public String adminStreamManage(HttpServletRequest request) {
        AdminResponse.StreamListDTO model = adminService.adminStreamManage();
        request.setAttribute("model", model);
        return "admin-stream-manage"; // mustache 파일명
    }

    // 유저 목록
    @GetMapping("/api/v1/admin/users")
    public String adminUserList(HttpServletRequest request) {
        AdminResponse.UserListDTO model = adminService.adminUserManage();
        request.setAttribute("model", model);
        return "admin-user-list";
    }

    // 신고 내역
    @GetMapping("/api/v1/admin/abusereports")
    public String adminReportList(HttpServletRequest request) {
        Map<String, Object> model = Map.of(
                "title", "신고 내역",
                "menu", Map.of("broadcast", false, "user", false, "report", true),
                "reports", List.of(
                        Map.of("id", 1, "reporterId", 10, "reportedBroadcastId", 100,
                                "reportedStreamerId", 20, "broadcastTitle", "게임 방송",
                                "streamerNickname", "스트리머1", "detailReason", "욕설 사용",
                                "reportType", "부적절한 언어", "broadcastTime", "2시간",
                                "requestTime", "2025-08-13 14:30")
                )
        );
        request.setAttribute("model", model);
        return "admin-report-list";
    }
}
