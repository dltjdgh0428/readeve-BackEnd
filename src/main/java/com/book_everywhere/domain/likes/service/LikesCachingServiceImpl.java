package com.book_everywhere.domain.likes.service;

import com.book_everywhere.domain.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesCachingServiceImpl implements LikesCachingService {

    private static final Logger logger = LoggerFactory.getLogger(LikesCachingServiceImpl.class);
    private final LikesRepository likesRepository;

    @Override
    @Cacheable(value = "likesCount", key = "#reviewId")
    public Long 좋아요캐시업데이트(Long reviewId) {
        logger.info(reviewId+"의 캐시가 업데이트 되었습니다.");
        return likesRepository.countByReviewId(reviewId);
    }

    @Override
    @CacheEvict(value = "likesCount", key = "#reviewId")
    public void 좋아요캐시무효화(Long reviewId) {

    }
}
