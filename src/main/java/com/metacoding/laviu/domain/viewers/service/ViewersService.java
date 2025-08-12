package com.metacoding.laviu.domain.viewers.service;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.viewers.domain.Viewers;
import com.metacoding.laviu.domain.viewers.domain.ViewersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewersService {

    private final ViewersRepository viewersRepository;

    @Transactional
    public void save(Streams stream, Users user) {
        //2. viewers 생성
        Viewers viewer = Viewers.builder()
                .user(user)
                .stream(stream)
                .build();
        //save
        Viewers viewersPS = viewersRepository.save(viewer);
    }
}
