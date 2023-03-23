package com.example.SEENEMA.logIn;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean  {
    private final JwtTokenProvider provider;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        // header에서 JWT 받기
        String token = provider.resolveToken((HttpServletRequest) request);
        // is valid?
        if(token != null && provider.validateToken(token)) {    // valid 검증
            // valid == true
            Authentication auth = provider.getAuthentication(token);  // 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(auth); // 인증 객체 저장
        }
        chain.doFilter(request,response);
    }
}
