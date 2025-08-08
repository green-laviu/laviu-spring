package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class StreamsServiceTest {

    @InjectMocks
    private StreamsService streamsService;  // 테스트할 대상

    // 의존하는 컴포넌트가 있다면 이렇게 Mock 선언
    @Mock
    private StreamsRepository streamRepository;
    @Mock
    private UsersRepository usersRepository;


    @Test
    public void verify_test() {
        Map<String, String> params = Map.of("streamKey", "test", "token", "test");
        System.out.println(streamsService.verify(params));
    }
}
