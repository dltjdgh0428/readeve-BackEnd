package com.book_everywhere.jwt.filter;

import com.book_everywhere.auth.dto.CustomOAuth2User;
import com.book_everywhere.auth.entity.Role;
import com.book_everywhere.jwt.dto.RefreshDto;
import com.book_everywhere.jwt.service.RefreshService;
import com.book_everywhere.jwt.token.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static com.book_everywhere.jwt.token.TokenType.ACCESS;
import static com.book_everywhere.jwt.token.TokenType.REFRESH;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshService refreshService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        Role role = Role.valueOf(auth.getAuthority());

        String access = jwtProvider.createJwt(ACCESS.getType(), username, role, ACCESS.getExpirationTime());
        String refresh = jwtProvider.createJwt(REFRESH.getType(), username, role, REFRESH.getExpirationTime());

        refreshService.리프레시토큰생성(new RefreshDto(username, refresh, REFRESH.getExpirationTime()));


//        response.sendRedirect("https://www.bookeverywhere.site/");
        response.setHeader(ACCESS.getType(), access);
        response.addCookie(jwtProvider.createCookie("Authorization", refresh));
//        response.addCookie(jwtProvider.createCookie(REFRESH.getType(), refresh));
        response.setStatus(HttpStatus.OK.value());
    }

}
