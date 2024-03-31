package com.book_everywhere.jwt.service;

import com.book_everywhere.jwt.domain.Refresh;
import com.book_everywhere.jwt.dto.RefreshDto;

public interface RefreshService {
    void 리프레시토큰삭제(String username);
    boolean 리프레시토큰조회(String refresh);
    RefreshDto 리프레시토큰객체조회(String refresh);
    void 리프레시토큰생성(RefreshDto refreshDto);
}
