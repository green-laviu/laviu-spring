package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class StreamsControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;
    private String viewerToken;
    private String viewer2Token;
    private String streamerToken;

    /**
     * 저장 streamer,viewer = 2번user  (이미 live인 스트리머는 방송 저장을 할수 없음으로 2번이 저장 스트리머이자 시청자로 테스트 진행/ viewerToken)
     * 보기 streamer =1번user (streamerToken)
     */
    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        Users cos = Users.builder()
                .id(2)
                .nickname("cos")
                .email("cos@nate.com")
                .roles("USER")
                .build();
        viewerToken = JwtUtil.create(cos);

        Users ssar = Users.builder()
                .id(1)
                .nickname("ssar")
                .email("ssar@nate.com")
                .roles("USER")
                .build();
        streamerToken = JwtUtil.create(ssar);

        Users good = Users.builder()
                .id(9)
                .nickname("good")
                .email("good@nate.com")
                .roles("USER")
                .build();
        viewer2Token = JwtUtil.create(good);

    }

    @Test
    public void end_test() throws Exception {
        //given
        Integer streamId = 1;

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/streams/" + streamId + "/end")
                        .header("Authorization", streamerToken)

        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.data").isEmpty());
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void save_test() throws Exception {
        //given
        StreamsRequest.SaveDTO reqDTO = new StreamsRequest.SaveDTO();
        reqDTO.setTitle("방송타이틀");
        reqDTO.setHashtagList(List.of("게임1", "방송1"));

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/streams/start")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", viewer2Token)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.data.streamId").value(12));
        actions.andExpect(jsonPath("$.data.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.title").value("방송타이틀"));
        actions.andExpect(jsonPath("$.data.status").value("PENDING"));

        actions.andExpect(jsonPath("$.data.hashtagList[0].hashtagId").value(5));
        actions.andExpect(jsonPath("$.data.hashtagList[0].hashtagName").value("게임1"));

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_test() throws Exception {
        //given
        Integer streamId = 1;

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/streams/{streamId}", streamId)
                        .header("Authorization", viewerToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));

        actions.andExpect(jsonPath("$.data.live.streamId").value(1));
        actions.andExpect(jsonPath("$.data.live.title").value("자바 기초 강의"));

        actions.andExpect(jsonPath("$.data.live.channel.streamer.userId").value(1));
        actions.andExpect(jsonPath("$.data.live.channel.streamer.nickname").value("ssar"));
        actions.andExpect(jsonPath("$.data.live.channel.streamer.profileImageUrl")
                .value("https://nate.com/profile1.jpg"));
        actions.andExpect(jsonPath("$.data.live.channel.streamer.email").value("ssar@nate.com"));
        actions.andExpect(jsonPath("$.data.live.channel.streamer.bio").value("안녕하세요"));
        actions.andExpect(jsonPath("$.data.live.channel.followerCount").value(2));
        actions.andExpect(jsonPath("$.data.live.channel.isFollowing").value(true));

        actions.andExpect(jsonPath("$.data.live.hlsUrl",
                matchesPattern("^/hls/[0-9a-zA-Z\\-_=]+\\.m3u8$"))); // Base64URL 패턴에 맞게 수정

        actions.andExpect(jsonPath("$.data.live.viewerCount").value(99));

        actions.andExpect(jsonPath("$.data.live.hashtagList[0].hashtagId").value(1));
        actions.andExpect(jsonPath("$.data.live.hashtagList[0].hashtagName").value("게임"));

        actions.andExpect(jsonPath("$.data.live.startedAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));

        actions.andExpect(jsonPath("$.data.live.streamKey",
                matchesPattern("^[0-9a-zA-Z\\-_=]+$"))); // Base64URL 캐릭터셋에 맞게 수정

        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }

    @Test
    public void get_streams_list_test() throws Exception {

        //given


        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/streams")
                        .header("Authorization", viewerToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        // HTTP 상태 및 기본 응답 검증
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));

        // 캐러셀 배열 (Carousel Array) 검증
        actions.andExpect(jsonPath("$.data.carousel").isArray());
        actions.andExpect(jsonPath("$.data.carousel[0].streamId").value(6));
        actions.andExpect(jsonPath("$.data.carousel[0].streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.carousel[0].streamer.userId").value(11));
        actions.andExpect(jsonPath("$.data.carousel[0].streamer.nickname").value("gameStreamer")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.carousel[0].streamer.profileImageUrl").value("https://nate.com/profile11.jpg")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.carousel[0].streamer.email").value("gameStreamer@nate.com"));
        actions.andExpect(jsonPath("$.data.carousel[0].streamer.bio").value("게임 방송합니다!"));
        actions.andExpect(jsonPath("$.data.carousel[0].title").value("[LIVE] 롤 랭크 게임 - 다이아 승급전!")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.carousel[0].viewerCount").value(1250)); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.carousel[0].thumbnailUrl").value("https://i.ytimg.com/vi/qXezAZT93So/maxresdefault.jpg")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.carousel[0].status").value("LIVE"));
        actions.andExpect(jsonPath("$.data.carousel[0].hashtagList").isEmpty()); // JSON 데이터에 맞게 수정

        // 추천 배열 (Recommended Array) 검증
        actions.andExpect(jsonPath("$.data.recommended").isArray());
        actions.andExpect(jsonPath("$.data.recommended[0].streamId").value(9)); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.recommended[0].streamer.userId").value(14)); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].streamer.nickname").value("fitnessCoach")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].streamer.profileImageUrl").value("https://nate.com/profile14.jpg")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].streamer.email").value("fitnessCoach@nate.com")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].streamer.bio").value("건강한 운동 라이프")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].title").value("💪 아침 홈트레이닝 따라하기")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].viewerCount").value(560)); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].thumbnailUrl").value("https://mblogthumb-phinf.pstatic.net/MjAxOTA3MTdfMjEg/MDAxNTYzMzM5NDAwMDE4.-EVRvfe331FwbM0B5LIPnFH0wwOBueF7Q_wqiKhNpFUg.IxXI5-0XIe8QR0ID9LZ4dxz0FIvQg_VvDEWHTKRXfgAg.JPEG.knoc3/%EC%8D%B8%EB%84%A4%EC%9D%BC.jpg?type=w800")); // JSON 데이터에 맞게 수정
        actions.andExpect(jsonPath("$.data.recommended[0].status").value("LIVE"));
        actions.andExpect(jsonPath("$.data.recommended[0].hashtagList").isEmpty()); // JSON 데이터에 맞게 수정

        // API 문서 생성 (andDo는 andExpect와 체이닝되지 않음)
        actions.andDo(MockMvcResultHandlers.print());
        actions.andDo(document);
    }

    @Test
    public void update_test() throws Exception {
        //given
        Integer streamId = 1;

        StreamsRequest.UpdateDTO reqDTO = new StreamsRequest.UpdateDTO();
        reqDTO.setTitle("방송타이틀5");
        reqDTO.setHashtagList(List.of("게임5", "방송5"));

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/streams/{streamId}/setting", streamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", streamerToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.data.streamId").value(1));
        actions.andExpect(jsonPath("$.data.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.title").value("방송타이틀5"));
        actions.andExpect(jsonPath("$.data.hashtagList[0].hashtagId").value(3));
        actions.andExpect(jsonPath("$.data.hashtagList[0].hashtagName").value("게임5"));
        actions.andExpect(jsonPath("$.data.status").value("LIVE"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
}
