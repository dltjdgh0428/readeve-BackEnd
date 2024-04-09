package com.book_everywhere.domain.likes.service;

import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final LikesCachingService likesCachingService;

    @Override
    @Transactional
    public void 좋아요(Long socialId, Long review_id) {
        User user = userRepository.findBySocialId(socialId).orElseThrow();
        likesRepository.mLike(user.getId(),review_id);
        likesCachingService.좋아요캐시무효화(review_id);
    }

    @Override
    @Transactional
    public void 좋아요취소(Long socialId, Long review_id) {
        User user = userRepository.findBySocialId(socialId).orElseThrow();
        likesRepository.mUnLike(user.getId(), review_id);
        likesCachingService.좋아요캐시무효화(review_id);
    }

}
