package com.metacoding.laviu.Streams;

import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
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

}
