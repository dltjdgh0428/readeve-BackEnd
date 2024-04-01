package com.book_everywhere.jwt.filter;

import com.book_everywhere.auth.dto.CustomOAuth2User;
import com.book_everywhere.auth.dto.UserDto;
import com.book_everywhere.auth.entity.Role;
import com.book_everywhere.jwt.token.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //여기서 나중에 front에서 엑세스토큰을 받아야함
        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
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

        //토큰 소멸 시간 검증
        if (jwtProvider.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtProvider.getUsername(token);
        Role role = jwtProvider.getRole(token);

//        String accessToken = request.getHeader(ACCESS.getType());
//
//        if (accessToken == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (!validateToken(response, accessToken)) {
//            return;
//        }
//
//        //토큰에서 username과 role 획득
//        UserDto userDto = new UserDto();
//        userDto.setNickname(jwtProvider.getUsername(accessToken));
//        userDto.setRole(jwtProvider.getRole(accessToken));

        //userDTO를 생성하여 값 set
        UserDto userDto = new UserDto();
        userDto.setNickname(username);
        userDto.setRole(role);

        logger.info("JWT 필터 인증 절차 ");
        logger.info("JWT 필터 인증 절차 ");
        logger.info("이곳은 앞의 헤더를 인증하여 세션에 사용자를 등록해줍니다. ");
        logger.info(String.valueOf(userDto));

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