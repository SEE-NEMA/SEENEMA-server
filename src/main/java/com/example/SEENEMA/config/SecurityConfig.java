package com.example.SEENEMA.config;

import com.example.SEENEMA.logIn.JwtAuthenticationFilter;
import com.example.SEENEMA.logIn.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider provider;

    // authenticationManager를 Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.httpBasic().disable(); // 일반적 루트 제외 거절
        http.httpBasic().disable()
                .authorizeRequests()    // 요청에 대한 사용권한 체크
                .antMatchers("/api/v1/test").authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
