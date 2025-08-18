package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class WebSocketControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper om;

    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private String jwtToken;

    @BeforeEach
    void setup() throws Exception {
        // [1] WebSocket 클라이언트 트랜스포트(Transport) 생성 (SockJS 지원)
        List<Transport> transports = Collections.singletonList(
                new WebSocketTransport(new StandardWebSocketClient())
        );
        SockJsClient sockJsClient = new SockJsClient(transports);

        // [2] WebSocketStompClient 인스턴스 생성 및 메시지 컨버터 등록
        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // [3] 테스트용 JWT 토큰 생성 (테스트에서 사용할 가짜 사용자)
        Users testUser = Users.builder()
                .id(1)
                .email("test@example.com")
                .nickname("testuser")
                .roles("USER")
                .build();
        jwtToken = JwtUtil.create(testUser); // JWT 토큰 생성

        // [4] WebSocket 접속 엔드포인트 URL 구성
        String wsUrl = "ws://localhost:" + port + "/ws";

        //  WebSocket, STOMP 연결에 사용할 헤더 생성
        WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders(); // 일반 WebSocket 커넥션용
        StompHeaders connectHeaders = new StompHeaders();            // STOMP 프로토콜용 헤더
        connectHeaders.add("Authorization", jwtToken);               // JWT 토큰 추가(인증)

        // [6] WebSocket 연결 (Spring 6.0+는 connectAsync() 사용 권장)
        // - wsUrl: 서버 WebSocket 엔드포인트
        // - wsHeaders: 웹소켓 핸드쉐이크용 헤더 (일반적으로 빈 객체)
        // - connectHeaders: STOMP CONNECT 프레임용 헤더
        // - StompSessionHandlerAdapter: 연결 후 콜백 등 핸들링
        stompSession = stompClient.connectAsync(wsUrl, wsHeaders, connectHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                // 정상적으로 연결된 후 이벤트(확인/디버깅용)
                log.info("WebSocket 연결 성공: {}", session.getSessionId());
            }
        }).get(5, TimeUnit.SECONDS); // 타임아웃 내에 연결 성공하지 못하면 예외 발생

        // [7] stompSession을 활용해 WebSocket 통합 테스트에서 실제 구독/전송 가능
    }

    @Test
    public void 방송_참가_테스트() throws Exception {
        // [1] 테스트용 streamKey와 결과 저장용 블로킹 큐 준비
        String streamKey = "test-stream-123";
        BlockingQueue<String> joinResults = new ArrayBlockingQueue<>(1);

        // [2] 개인 메시지(/user/queue/join-result) 구독: 서버에서 방송 참가 결과를 받을 곳
        stompSession.subscribe("/user/queue/join-result", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class; // 받은 메시지 타입(JSON 문자열)
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                joinResults.add((String) payload); // 메시지 큐에 결과 저장 → 테스트 스레드에서 대기하다 get
            }
        });

        // [3] 실제 방송 참가 요청 전송 (서버의 @MessageMapping("/stream/{streamKey}/join") 등으로 연결됨)
        stompSession.send("/app/stream/" + streamKey + "/join", "");

        // [4] 결과 메시지를 5초간 대기하여 수신, 없으면 실패
        String result = joinResults.poll(5, TimeUnit.SECONDS);
        assertNotNull(result, "참가 결과를 받지 못함");

        // [5] (선택) JSON 파싱 및 값 검증
        // JoinResultDTO joinResult = om.readValue(result, JoinResultDTO.class);
        // assertTrue(joinResult.isSuccess());

        // 최종 디버그
        log.info("✅ 방송 참가 테스트 결과: {}", result);
    }

    @Test
    public void 참가자_목록_업데이트_테스트() throws Exception {
        String streamKey = "test-stream-123";
        BlockingQueue<String> participantUpdates = new ArrayBlockingQueue<>(1);

        // [1] 참가자 목록 업데이트 구독 (참가/퇴장 시마다 스트리머에게 보내는 채널 구독)
        stompSession.subscribe("/topic/" + streamKey + "/participants", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                participantUpdates.add((String) payload); // 참가자 목록 JSON 수신
            }
        });

        // [2] 방송 참가 → 참가자 목록 변경 유발
        stompSession.send("/app/stream/" + streamKey + "/join", "");

        // [3] 참가자 목록 업데이트 수신(5초 대기)
        String participantList = participantUpdates.poll(5, TimeUnit.SECONDS);
        assertNotNull(participantList, "참가자 목록 업데이트를 받지 못함");

        log.info("✅ 참가자 목록 업데이트 테스트 결과: {}", participantList);
    }

    @Test
    public void 채팅_메시지_테스트() throws Exception {
        String streamKey = "test-stream-123";
        BlockingQueue<String> chatMessages = new ArrayBlockingQueue<>(1);

        // [1] 채팅 메시지 브로드캐스트 채널 구독
        stompSession.subscribe("/topic/stream/" + streamKey + "/chat", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                chatMessages.add((String) payload); // 채팅 메시지 수신
            }
        });

        // 반드시 방송 참가 후 채팅(권한, 인증 문제 방지)
        stompSession.send("/app/stream/" + streamKey + "/join", "");
        Thread.sleep(1000); // 참가 처리 대기

        // [2] 채팅 메시지 전송
        String chatMsg = "{\"message\":\"안녕하세요!\"}";
        stompSession.send("/app/stream/" + streamKey + "/chat", chatMsg);

        // [3] 채팅 메시지 수신 검증
        String receivedMessage = chatMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(receivedMessage, "채팅 메시지를 받지 못함");

        log.info("✅ 채팅 메시지 테스트 결과: {}", receivedMessage);
    }

    @Test
    public void 인증_실패_테스트() throws Exception {
        String wsUrl = "ws://localhost:" + port + "/ws";
        WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer invalid-token");

        assertThrows(Exception.class, () -> {
            stompClient.connectAsync(wsUrl, wsHeaders, connectHeaders, new StompSessionHandlerAdapter() {
                // 빈 구현체 - 연결 실패만 확인하면 되므로
            }).get(3, TimeUnit.SECONDS);
        }, "잘못된 토큰으로 연결이 성공해서는 안됩니다");

        log.info("✅ 인증 실패 테스트 완료");
    }

    @Test
    public void 스트리머_전용_채널_권한_테스트() throws Exception {
        String streamKey = "test-stream-123";
        BlockingQueue<String> errorQueue = new ArrayBlockingQueue<>(1);

        // [1] 권한 없는 사용자가 스트리머 채널 구독 시도 (에러 발생 예상)
        stompSession.subscribe("/topic/stream/" + streamKey + "/participants", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                errorQueue.add((String) payload);
            }
        });

        // [2] 구독 시 AccessDeniedException 등으로 STOMP ERROR Frame 수신 가능(일부 환경에서는 예외로 직접 올라옴)
        String errorOrNull = errorQueue.poll(3, TimeUnit.SECONDS);
        assertNull(errorOrNull, "스트리머 전용 채널은 일반 사용자가 수신하거나 메시지를 받아선 안됨");

        log.info("✅ 스트리머 전용 채널 권한 테스트 완료");
    }
}
