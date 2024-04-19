package com.book_everywhere.domain.follow.controller;

import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.domain.auth.dto.CustomOAuth2User;
import com.book_everywhere.domain.follow.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/subscribe/{toUserId}")
    public CMRespDto<?> subscribe(@AuthenticationPrincipal CustomOAuth2User principalDetails, @PathVariable Long toUserId) {
//        int result = subscribeService.구독하기(principalDetails.getUser().getId(), toUserId);
        return new CMRespDto<>(HttpStatus.OK, 0, "구독 성공!");
    }

    @DeleteMapping("/subscribe/{toUserId}")
    public CMRespDto<?> unSubscribe(@AuthenticationPrincipal CustomOAuth2User principalDetails, @PathVariable Long toUserId) {
//        int result = subscribeService.구독취소(principalDetails.getUser().getId(), toUserId);
        return new CMRespDto<>(HttpStatus.OK, 0, "구독 취소 성공!");
    }
}
