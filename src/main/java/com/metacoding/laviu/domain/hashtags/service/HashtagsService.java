package com.metacoding.laviu.domain.hashtags.service;

import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.HashtagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HashtagsService {
    private final HashtagsRepository hashtagsRepository;

    @Transactional
    public Hashtags save(String hashtagName) {
        Optional<Hashtags> hashtag = hashtagsRepository.findByName(hashtagName);
        return hashtag.orElseGet(() -> hashtagsRepository.save(Hashtags.builder().name(hashtagName).build()));
    }
}
