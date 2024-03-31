package com.book_everywhere.jwt.dto;

import com.book_everywhere.jwt.domain.Refresh;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class RefreshDto {
    private String username;
    private String refresh;
    private Long expiredMs;

    public Refresh toEntity(Date date) {
        return Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();
    }

    public static RefreshDto toDto(Refresh refresh) {
        return RefreshDto.builder()
                .username(refresh.getUsername())
                .refresh(refresh.getRefresh())
                .expiredMs(Long.parseLong(refresh.getExpiration()))
                .build();
    }
}
