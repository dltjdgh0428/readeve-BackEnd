package com.book_everywhere.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService{
    @Override
    public int 구독하기(Long fromUserId, Long toUserId) {
        return 0;
    }

    @Override
    public int 구독취소(Long fromUserId, Long toUserId) {
        return 0;
    }
}
