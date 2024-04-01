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
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

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

        if (jwtProvider.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtProvider.getUsername(token);
        String role = jwtProvider.getRole(token);

        //userDTO를 생성하여 값 set
        UserDto userDto = new UserDto();
        userDto.setNickname(username);
        userDto.setRole(role);


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