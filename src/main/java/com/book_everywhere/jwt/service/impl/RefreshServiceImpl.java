package com.book_everywhere.jwt.service.impl;

import com.book_everywhere.jwt.domain.Refresh;
import com.book_everywhere.jwt.dto.RefreshDto;
import com.book_everywhere.jwt.repository.RefreshRepository;
import com.book_everywhere.jwt.service.RefreshService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final RefreshRepository refreshRepository;

    @Override
    public void 리프레시토큰삭제(String username) {
        refreshRepository.deleteByUsername(username);
    }

    @Override
    public boolean 리프레시토큰조회(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }

    @Override
    public void 리프레시토큰생성(RefreshDto refreshDto) {
        Date date = new Date(System.currentTimeMillis() + refreshDto.getExpiredMs());
        refreshRepository.save(refreshDto.toEntity(date));
    }

    @Override
    public RefreshDto 리프레시토큰객체조회(String refresh) {
        Refresh token =refreshRepository.findByRefresh(refresh);
        return RefreshDto.toDto(token);
    }
}
