package com.book_everywhere.jwt.controller;

import com.book_everywhere.auth.entity.Role;
import com.book_everywhere.jwt.dto.RefreshDto;
import com.book_everywhere.jwt.service.RefreshService;
import com.book_everywhere.jwt.token.JwtProvider;
import com.book_everywhere.common.dto.CMRespDto;
import com.book_everywhere.jwt.token.TokenType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.book_everywhere.jwt.token.TokenType.ACCESS;
import static com.book_everywhere.jwt.token.TokenType.REFRESH;


@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final JwtProvider jwtProvider;
    private final RefreshService refreshService;

    @PostMapping("/api/refresh")
    public CMRespDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH.getType())) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            //response status code
            return new CMRespDto<>(HttpStatus.BAD_REQUEST, null, "refresh token null");
        }

        //expired check
        try {
            jwtProvider.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new CMRespDto<>(HttpStatus.BAD_REQUEST, null, "refresh token expired");
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtProvider.getCategory(refresh);

        if (!category.equals(REFRESH.getType())) {
            return new CMRespDto<>(HttpStatus.BAD_REQUEST, null, "invalid refresh token");
        }

        boolean isExist = refreshService.리프레시토큰조회(refresh);
        if (!isExist) {
            return new CMRespDto<>(HttpStatus.BAD_REQUEST, null, "invalid refresh token");
        }


        String username = jwtProvider.getUsername(refresh);
        String role = jwtProvider.getRole(refresh);

        //make new JWT
//        String newAccess = jwtProvider.createJwt(ACCESS.getType(), username, role, ACCESS.getExpirationTime());
//        String newRefresh = jwtProvider.createJwt(REFRESH.getType(), username, role, REFRESH.getExpirationTime());
//
//        refreshService.리프레시토큰삭제(refresh);
//        refreshService.리프레시토큰생성(new RefreshDto(username, newRefresh, REFRESH.getExpirationTime()));
//
//        //response
//        response.setHeader(ACCESS.getType(), newAccess);
//        response.addCookie(jwtProvider.createCookie(REFRESH.getType(), newRefresh));

        return new CMRespDto<>(HttpStatus.OK, null, "재발급 완료");
    }


}