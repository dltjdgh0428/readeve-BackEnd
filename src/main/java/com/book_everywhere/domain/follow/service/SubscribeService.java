package com.book_everywhere.domain.follow.service;

public interface SubscribeService {

    int 구독하기(Long fromUserId, Long toUserId);

    int 구독취소(Long fromUserId, Long toUserId);
}
