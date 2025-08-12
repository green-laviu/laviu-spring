package com.metacoding.laviu.Streams;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessages;
import com.metacoding.laviu.domain.chatmessages.domain.ChatMessagesRepository;
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
import com.metacoding.laviu.domain.viewers.dto.ViewersResponse;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @Autowired
    private ChatMessagesRepository chatMessagesRepository;
    @Autowired
    private ViewersService viewersService;

    @Test
    public void save_test() {

        //given
        Users user = new Users(1);
        StreamsRequest.SaveDTO reqDTO = new StreamsRequest.SaveDTO();
        reqDTO.setTitle("제목");
        reqDTO.setHashtags(List.of("소통", "게임"));

        //when
        StreamsResponse.SaveDTO respDTO = streamsService.save(reqDTO, user);
        System.out.println("스트림키 : " + respDTO.getStreamKey());
        System.out.println("id : " + respDTO.getId());
        System.out.println("상태 : " + respDTO.getStatus());
        System.out.println("해시 : " + respDTO.getHashtags());

    }

    @Test
    public void detail_test() {
        int id = 2;
        Users user = new Users(id);


        // 제재상태면 못봄 - 강퇴

        // 1.streams 테이블 조회 및 인증 체크 (STREAMID면서 LIVE인게 있는지 확인)
        Streams stream = streamsRepository.findByIdJoinStreamer(1)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.STREAM_NOT_FOUND));

        //2.viewer save
        viewersService.save(stream, user);

        //3. 스트림 테이블 뷰업테이트
        stream.upViewerCount();

        System.out.println("---------1차--------");
        System.out.println(" 뷰어 수 : " + stream.getViewerCount());


        //4.팔로워수 팔로워 여부 , 채널주 정보 생성
        Long followerCount = followsRepository.countByFollowingId(stream.getStreamer().getId());
        Boolean isFollowing = followsRepository.existsByFollowerIdAndFollowingId(stream.getStreamer().getId(), user.getId());
        UsersResponse.ChannelInfoDTO channel = new UsersResponse.ChannelInfoDTO(stream.getStreamer(), followerCount, isFollowing);

        System.out.println("----------2차---------");
        System.out.println(" 팔로워수 : " + channel.getFollowerCount());
        System.out.println("팔로워 여부" + channel.getIsFollowing());
        System.out.println("스트리머" + channel.getNickname());

        //5.채팅
        List<ChatMessages> chatList = chatMessagesRepository.findAllByStreamIdJoinUser(stream.getId());
        List<ChatMessagesResponse.ChatDetailDTO> chatResultList = ChatMessagesResponse.ChatDetailDTO.fromList(chatList);

        System.out.println("----3차----");
        System.out.println(" 채팅자 이메일 : " + chatResultList.get(1).getEmail());
        System.out.println("채팅자 " + chatResultList.get(1).getContent());


        //6.hlsUrl
        String hlsUrl = "http://host/hls/" + stream.getStreamKey() + ".m3u8";

        //7.HASHTAGE  TODO(삭제요망/임의데이터)
        List<StreamHashtags> streamHashtagList = List.of(
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("소통").build())
                        .build(),
                StreamHashtags.builder()
                        .stream(stream)
                        .hashtag(Hashtags.builder().name("게임").build())
                        .build()
        );

        //8.viewer 목록 (id)
        List<Viewers> viewerList = viewersRepository.findAllByStreamId(stream.getId());
        List<ViewersResponse.ViewersDetailDTO> viewerResultList = ViewersResponse.ViewersDetailDTO.fromList(viewerList);

        System.out.println("-----------3차-------");
        System.out.println(" 시청자 이메일 : " + viewerResultList.get(1).getEmail());

        //9.라이브정보 합치기
        LiveDetailDTO live = new LiveDetailDTO(stream, channel, hlsUrl, streamHashtagList);

        System.out.println("----------4차----------");
        System.out.println(" 화질 : " + live.getQualityOptions());

        //전체 maindetaildto에 담기 (라이브정보 +채팅정보 + 뷰어리스트)
        StreamsResponse.DetailDTO resDTO = new StreamsResponse.DetailDTO(chatResultList, live, viewerResultList);

        System.out.println("----------5차-----------");
        System.out.println(" 채팅 닉네임 : " + resDTO.getChatList().get(1).getNickname());
        System.out.println(" 채팅 닉네임 : " + resDTO.getChatList().get(1).getEmail());
        System.out.println("시청자 이메일" + resDTO.getViewerList().get(1).getEmail());
        System.out.println("채널 이름" + resDTO.getLive().getTitle());
        System.out.println("채널 팔로워 수 " + resDTO.getLive().getChannel().getFollowerCount());

    }


}
