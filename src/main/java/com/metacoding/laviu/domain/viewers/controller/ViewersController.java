package com.metacoding.laviu.domain.viewers.controller;

import com.metacoding.laviu._core.utils.Resp;
import com.metacoding.laviu.domain.viewers.service.ViewersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s/api/v1/viewers")
public class ViewersController {
    private final ViewersService viewersService;

    @DeleteMapping("/{viewerId}")
    public ResponseEntity<?> delete(@PathVariable String viewerId) {
        viewersService.delete(viewerId);
        return Resp.ok(null);
    }
}
