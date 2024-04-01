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
import java.io.PrintWriter;

import static com.book_everywhere.jwt.token.TokenType.ACCESS;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //여기서 헤더가 access인
        String accessToken = request.getHeader(ACCESS.getType());


//        String accessToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            // 쿠키가 없음을 처리하는 로직
//            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
            logger.info(accessToken);
//            logger.info("쿠키없다는데? 왜없냐 ㄹㅇ");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        for (Cookie cookie : cookies) {
//            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
//            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
//            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
//            logger.info("@@@@@@@@@@@@@@@@@@@@@@");
//            logger.info(cookie);
//            if (cookie.getName().equals(ACCESS.getType())) {
//                accessToken = cookie.getValue();
//            }
//        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!validateToken(response, accessToken)) {
            return;
        }

        //토큰에서 username과 role 획득
        UserDto userDto = new UserDto();
        userDto.setNickname(jwtProvider.getUsername(accessToken));
        userDto.setRole(jwtProvider.getRole(accessToken));

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean validateToken(HttpServletResponse response, String accessToken) throws IOException {
        try {
            if (jwtProvider.isExpired(accessToken) || !ACCESS.getType().equals(jwtProvider.getCategory(accessToken))) {
                sendErrorResponse(response, "Invalid or expired access token", HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        } catch (Exception e) { // 넓은 범위의 예외 처리를 통해 다양한 에러 상황을 처리할 수 있습니다.
            sendErrorResponse(response, "Token validation error", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(message);
        }
    }
}