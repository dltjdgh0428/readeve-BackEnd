package com.book_everywhere.jwt.filter;

import com.book_everywhere.auth.dto.CustomOAuth2User;
import com.book_everywhere.auth.dto.UserDto;
import com.book_everywhere.jwt.token.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //여기서 헤더가 access인
        String authorization = null;

        Cookie[] cookies = request.getCookies();
        StringBuilder message = new StringBuilder();
        message.append("Request Method: ").append(request.getMethod())
                .append(", URL: ").append(request.getRequestURL());

        // 헤더 정보 로깅
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                message.append(", ").append(headerName).append(": ").append(request.getHeader(headerName))
        );

        // 파라미터 정보 로깅 (선택적)
        request.getParameterMap().forEach((key, value) ->
                message.append(", ").append(key).append(": ").append(Arrays.toString(value))
        );

        logger.info(message.toString());
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info(cookie.toString());
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        } else {
            logger.info("쿠키가 없습니다.");
        }

        //Authorization 헤더 검증
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        if (jwtProvider.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        UserDto userDto = new UserDto();
        userDto.setNickname(jwtProvider.getUsername(authorization));
        userDto.setRole(jwtProvider.getRole(authorization));

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}