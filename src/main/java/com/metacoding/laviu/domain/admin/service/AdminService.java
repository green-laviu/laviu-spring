package com.metacoding.laviu.domain.admin.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.admin.domain.AdminRepository;
import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    // 로그인
    public AdminResponse.LoginDTO login(AdminRequest.LoginDTO reqDTO) {
        Users user = adminRepository.getByEmailAndType(reqDTO.getEmail(), UsersType.ADMIN);
        if (user == null) throw new ExceptionApi404(ErrorEnum.USER_NOT_FOUND);

        String password = user.getPassword();
        if (password == null || !password.equals(reqDTO.getPassword())) {
            throw new ExceptionApi404(ErrorEnum.USER_NOT_FOUND);
        }

        if (user.getType() != UsersType.ADMIN) {
            throw new ExceptionApi404(ErrorEnum.ADMIN_PRIVILEGE_REQUIRED);
        }
        AdminResponse.LoginDTO respDTO = AdminResponse.LoginDTO.of(user);
        return respDTO;
    }

    // 실시간 방송 관리
    public void adminStreamManage() {

    }

}
