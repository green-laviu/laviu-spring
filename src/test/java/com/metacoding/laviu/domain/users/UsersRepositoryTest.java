package com.metacoding.laviu.domain.users;


import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersRepositoryTest {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void returnStreamerDTO_test() {
        UsersResponse.StreamerDTO result = usersRepository.returnStreamerDTO(1, 2);
        System.out.println("반환값 : " + result.toString());
    }
}
