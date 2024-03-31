package com.book_everywhere.jwt.token;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS("access", 600000L),
    REFRESH("refresh", 86400000L);

    private final String type;
    private final Long expirationTime;

    TokenType(String type, Long expirationTime) {
        this.type = type;
        this.expirationTime = expirationTime;
    }

}
