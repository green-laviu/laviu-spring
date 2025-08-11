package com.metacoding.laviu.Streams;

import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu.domain.chatmessages.dto.ChatMessagesResponse;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.LiveDetailDTO;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.FollowsRepository;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import com.metacoding.laviu.domain.viewers.dto.ViewersRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.metacoding.laviu._core.error.ErrorEnum.NO_LIVE_STREAMING;


@SpringBootTest
public class StreamsTest {

    @Autowired
    private StreamsService streamsService;
    @Autowired
    private StreamsRepository streamsRepository;
    @Autowired
    private ViewersRepository viewersRepository;
    @Autowired
    private FollowsRepository followsRepository;

    @Test
    public void save_test() {

        //given
        Users user = new Users(1);
        StreamsRequest.SaveDTO reqDTO = new StreamsRequest.SaveDTO("제목", List.of("소통", "게임"));

        //when
        StreamsResponse.SaveDTO respDTO = streamsService.save(reqDTO, user);
        System.out.println("스트림키 : " + respDTO.getStreamKey());
        System.out.println("id : " + respDTO.getId());
        System.out.println("상태 : " +  respDTO.getStatus());
        System.out.println("해시 : " + respDTO.getHashtags());

    }
    @Test
    public void detail() {
        int id = 2;
        Users user = new Users(id);


        // 1.streams 테이블 조회 및 인증 체크 (STREAMID면서 LIVE인게 있는지 확인)
        Streams stream = streamsRepository.findByIdJoinUser(1).orElseThrow(() -> new ExceptionApi400(NO_LIVE_STREAMING));

        //eyes
        System.out.println("id : " + stream.toString());

        stream.updateviewerCount();

        Viewers viewers = viewersRepository.save(new ViewersRequest.SaveDTO().toEntity(stream, user));

        Long followerCount = followsRepository.countByFollowingId(stream.getStreamer());
        boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(stream.getStreamer(), user);
        UsersResponse.LiveDetailDTO channel = new UsersResponse.LiveDetailDTO(stream.getStreamer(), followerCount, isFollowing);
        System.out.println("수" + followerCount);
        System.out.println("팔로우" + isFollowing);
        System.out.println("channel : " + channel.getFollowerCount());
        System.out.println("ch" + channel.getNickname());

        //5.채팅 테이블 정보 받기 TODO
        List<ChatMessagesResponse.ChatDetailDTO> chatListDTO = List.of(
                new ChatMessagesResponse.ChatDetailDTO(1, 1, 1, "오 무야호ㅋㅋㅋ", LocalDateTime.now().minusSeconds(50)),
                new ChatMessagesResponse.ChatDetailDTO(2, 1, 2, "진짜 재밌다", LocalDateTime.now().minusSeconds(30)),
                new ChatMessagesResponse.ChatDetailDTO(3, 1, 3, "와 화질좋다 1080p!", LocalDateTime.now().minusSeconds(10))
        );
        //6.
        String hlsUrl = "111";

        //7.HASHTAGE
        List<StreamHashtags> streamHashtags = List.of(
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("소통").build())
                        .build(),
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("게임").build())
                        .build()
        );



        //라이브정보 합치기
        LiveDetailDTO live = new LiveDetailDTO (stream, channel, hlsUrl, streamHashtags);
        //전체 maindto에 담기
        StreamsResponse.DetailDTO resDTO = new StreamsResponse.DetailDTO( chatListDTO,live);

        System.out.println(resDTO.getChatList().size());
        System.out.println("업데이트" + resDTO.getLive().getViewers());

    }

}
