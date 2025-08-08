package com.metacoding.laviu.domain.streams.controller;

import com.metacoding.laviu.domain.streams.service.StreamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/streams")
public class StreamsController {

    private final StreamsService streamsService;


}
