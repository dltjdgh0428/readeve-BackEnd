package com.book_everywhere.domain.follow.controller;

import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.domain.auth.dto.CustomOAuth2User;
import com.book_everywhere.domain.follow.dto.SubscribeRespDto;
import com.book_everywhere.domain.follow.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("api/subscribe/{toUserSocialId}")
    public CMRespDto<?> subscribe(@AuthenticationPrincipal CustomOAuth2User principalDetails, @PathVariable Long toUserSocialId) {
        subscribeService.구독하기(principalDetails.getSocialId(), toUserSocialId);
        return new CMRespDto<>(HttpStatus.OK, null, "구독 성공!");
    }

    @DeleteMapping("api/subscribe/{toUserSocialId}")
    public CMRespDto<?> unSubscribe(@AuthenticationPrincipal CustomOAuth2User principalDetails, @PathVariable Long toUserSocialId) {
        subscribeService.구독취소(principalDetails.getSocialId(), toUserSocialId);
        return new CMRespDto<>(HttpStatus.OK, null, "구독 취소 성공!");
    }

    @GetMapping("api/subscribe/{pageUserSocialId}") // data 리턴하는 것
    public CMRespDto<?> followList(@AuthenticationPrincipal CustomOAuth2User principalDetails, @PathVariable Long pageUserSocialId) {
        List<SubscribeRespDto> subscribeRespDto = subscribeService.구독리스트(principalDetails.getSocialId(), pageUserSocialId);
        return new CMRespDto<>(HttpStatus.OK, subscribeRespDto, "구독자 리스트 반환 성공!");
    }
}
