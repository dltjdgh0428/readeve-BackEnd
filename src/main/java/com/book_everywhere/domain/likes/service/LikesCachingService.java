package com.book_everywhere.domain.likes.service;

public interface LikesCachingService {

    Long 좋아요캐시업데이트(Long reviewId);

    void 좋아요캐시무효화(Long reviewId);
}
