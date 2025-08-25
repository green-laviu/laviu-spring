package com.metacoding.laviu.domain.admin.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi401;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReports;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsRepository;
import com.metacoding.laviu.domain.abusereports.domain.AbuseReportsStatus;
import com.metacoding.laviu.domain.admin.dto.AdminRequest;
import com.metacoding.laviu.domain.admin.dto.AdminResponse;
import com.metacoding.laviu.domain.chatmessages.service.ChatMessagesService;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.domain.UsersType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final StreamsRepository streamsRepository;
    private final AbuseReportsRepository abusereportsRepository;
    private final StreamsService streamsService;
    private final ChatMessagesService chatMessagesService;


    /**
     * 관리자 로그인 기능을 처리하는 메서드.
     * 이메일과 비밀번호로 관리자 유저를 인증하고, 성공 시 로그인 정보를 담은 DTO를 반환합니다.
     *
     * @param reqDTO 로그인 요청 데이터(이메일, 비밀번호)
     * @return 로그인 성공 시, 관리자 정보를 담은 DTO
     * @throws ExceptionApi404, ExceptionApi403 유저를 찾을 수 없거나 비밀번호가 일치하지 않을 때, 또는 관리자 권한이 없을 때 발생
     */
    public AdminResponse.LoginDTO login(AdminRequest.LoginDTO reqDTO) {

        // 1. 이메일과 관리자(ADMIN) 타입으로 유저를 조회
        Users user = usersRepository.getByEmailAndType(reqDTO.getEmail(), UsersType.ADMIN)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));

        boolean isSame = BCrypt.checkpw(reqDTO.getPassword(), user.getPassword());

        // 2. 요청 비밀번호와 DB에 저장된 비밀번호를 비교
        if (!isSame) {
            throw new ExceptionApi404(ErrorEnum.USER_NOT_FOUND);
        }

        // 3. 유저의 타입이 ADMIN인지 다시 한 번 확인합니다. (getByEmailAndType에서 이미 확인했지만, 방어적 코드)
        if (!user.getRoles().contains(UsersType.ADMIN.name())) {
            throw new ExceptionApi401(ErrorEnum.ADMIN_PRIVILEGE_REQUIRED);
        }

        // 4. 모든 검증을 통과하면 AdminResponse.LoginDTO를 생성하여 반환합니다.
        AdminResponse.LoginDTO respDTO = new AdminResponse.LoginDTO(user);
        return respDTO;
    }

    /**
     * 관리자 페이지의 실시간 방송 관리 화면에 필요한 데이터를 제공하는 메서드.
     * 현재 LIVE 상태인 모든 스트림 목록을 조회하여 DTO에 담아 반환합니다.
     *
     * @return 실시간 방송 목록과 페이지 정보를 담은 DTO
     */
    public AdminResponse.StreamListDTO adminStreamList() {

        // 1. StreamsRepository를 사용하여 LIVE 상태인 스트림 목록을 조회
        List<Streams> liveStreams = streamsRepository.findAllLiveStreamsWithStreamer();

        // 2. AdminResponse.StreamManageDTO 객체를 생성하고, 페이지에 필요한 기본 정보를 설정
        AdminResponse.StreamListDTO respDTO = new AdminResponse.StreamListDTO();

        // DTO의 menu 필드(Map<String, Boolean>)에 "broadcast"라는 키와 true라는 값을 추가하여,
        // 뷰(Mustache)에서 이 값을 참조해 해당 메뉴에 checked 속성을 부여
        respDTO.getMenu().put("broadcast", true);

        // 3. 조회한 Streams 엔티티 리스트를 StreamManageDTO의 내부 클래스인 Stream DTO 리스트로 변환
        List<AdminResponse.StreamListDTO.Stream> dtoList = liveStreams.stream()
                .map(AdminResponse.StreamListDTO.Stream::new)
                .collect(Collectors.toList());

        // 4. 변환된 DTO 리스트를 streamManageDTO에 설정
        respDTO.setStreams(dtoList);

        // 5. 최종 완성된 DTO를 반환
        return respDTO;
    }

    /**
     * 관리자 페이지의 유저 관리 화면에 필요한 데이터를 제공하는 메서드.
     * 모든 유저 목록을 조회하여 DTO에 담아 반환합니다.
     *
     * @return 유저 목록과 페이지 정보를 담은 DTO
     */
    public AdminResponse.UserListDTO adminUserList() {
        // 1. UsersRepository를 사용하여 모든 유저 목록을 조회
        List<Users> users = usersRepository.getAllUsers();

        // 2. AdminResponse.UserManageDTO 객체를 생성하고, 페이지에 필요한 기본 정보를 설정
        AdminResponse.UserListDTO respDTO = new AdminResponse.UserListDTO();

        // DTO의 menu 필드(Map<String, Boolean>)에 "user"라는 키와 true라는 값을 추가하여,
        // 뷰(Mustache)에서 이 값을 참조해 해당 메뉴에 checked 속성을 부여
        respDTO.getMenu().put("user", true);

        // 3. 조회한 Users 엔티티 리스트를 UserManageDTO의 내부 클래스인 User DTO 리스트로 변환
        List<AdminResponse.UserListDTO.User> dtoList = users.stream()
                .map(AdminResponse.UserListDTO.User::new)
                .collect(Collectors.toList());

        // 4. 변환된 DTO 리스트를 userManageDTO에 설정
        respDTO.setUsers(dtoList);

        // 5. 최종 완성된 DTO를 반환
        return respDTO;
    }

    /**
     * 관리자 페이지의 신고 내역 화면에 필요한 데이터를 제공하는 메서드.
     * 모든 신고 목록을 조회하여 DTO에 담아 반환합니다.
     *
     * @return 신고 목록과 페이지 정보를 담은 DTO
     */
    public AdminResponse.ReportListDTO adminReportList() {
        // 1. AbuseReportsRepository를 사용하여 모든 신고 목록을 조회
        List<AbuseReports> abuseReports = abusereportsRepository.findAll();

        // 2. AdminResponse.ReportListDTO 객체를 생성하고, 페이지에 필요한 기본 정보를 설정
        AdminResponse.ReportListDTO respDTO = new AdminResponse.ReportListDTO();

        // DTO의 menu 필드(Map<String, Boolean>)에 "report"라는 키와 true라는 값을 추가하여,
        // 뷰(Mustache)에서 이 값을 참조해 해당 메뉴에 checked 속성을 부여
        respDTO.getMenu().put("report", true);

        // 3. 조회한 AbuseReports 엔티티 리스트를 ReportListDTO의 내부 클래스인 Report DTO 리스트로 변환
        List<AdminResponse.ReportListDTO.Report> dtoList = abuseReports.stream()
                .map(AdminResponse.ReportListDTO.Report::new)
                .collect(Collectors.toList());

        // 4. 변환된 DTO 리스트를 respDTO에 설정
        respDTO.setReports(dtoList);

        // 5. 최종 완성된 DTO를 반환
        return respDTO;
    }

    @Transactional
    public void processAbuseReport(Integer reportId, String status) {
        // 1. 해당 신고를 찾기
        AbuseReports abuseReports = abusereportsRepository.findById(reportId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.REPORT_NOT_FOUND));

        // 2. 받은 문자열 상태를 Enum 타입으로 변환
        AbuseReportsStatus updateStatus = AbuseReportsStatus.valueOf(status);

        // 3. 엔티티의 상태를 변경하는 메서드 호출
        abuseReports.updateStatus(updateStatus);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.getByEmail(username)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.USER_NOT_FOUND));
    }

    /**
     * 관리자 권한으로 방송을 종료하고, 클라이언트에게 메시지를 전송합니다.
     *
     * @param streamId 종료할 방송의 ID
     */
    @Transactional
    public void adminStreamEnd(Integer streamId) {
        log.debug("AdminService - 방송 종료 시작");

        // 1. StreamsService를 통해 방송 상태를 '종료'로 변경하고, streamKey 반환
        String streamKey = streamsService.adminEndStream(streamId);

        // 2. ChatMessagesService를 통해 웹소켓 메시지 전송
        chatMessagesService.sendStreamEndMessage(streamKey);

        log.debug("AdminService - 방송 종료 완료");
    }
}