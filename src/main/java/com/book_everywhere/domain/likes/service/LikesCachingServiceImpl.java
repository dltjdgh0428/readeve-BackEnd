package com.book_everywhere.domain.likes.service;

import com.book_everywhere.domain.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesCachingServiceImpl implements LikesCachingService {

    private final LikesRepository likesRepository;

    @Override
    @CachePut(value = "likesCount", key = "#reviewId")
    public Long 좋아요캐시업데이트(Long reviewId) {
        return likesRepository.countByReviewId(reviewId);
    }

    @Override
    @CacheEvict(value = "likesCount", key = "#reviewId")
    public void 좋아요캐시무효화(Long reviewId) {

    }
}
