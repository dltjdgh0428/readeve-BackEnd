package com.book_everywhere.jwt.repository;

import com.book_everywhere.jwt.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

    //유저네임 기준으로 삭제 이러면 모든 곳에서 로그아웃됨.
    void deleteByUsername(String username);

    Refresh findByRefresh(String refresh);
}
