package com.example.SEENEMA.config;

import com.example.SEENEMA.jwt.JwtAuthenticationFilter;
import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.oauth.service.OAuthService;
import com.example.SEENEMA.user.service.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider provider;
    private final PrincipalDetailsService service;
    private final OAuthService oAuthService;

    // 암호화에 필요한거 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 인증manager 빈 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{ // 인증 필요한 페이지 여기 추가하고, role 추가해주기
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/v1/test/**").hasRole("USER");
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/test/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .logout().logoutSuccessUrl("/api/v1/")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/api/v1/test/oauth/loginInfo", true)
                .userInfoEndpoint()
                .userService(service);
//                .formLogin()
//                .loginPage("/api/v1/user/test/loginForm")
//                .loginProcessingUrl("/api/v1/user/test/login")
//                .defaultSuccessUrl("/api/v1/")
//                .and()
//                .oauth2Login()
//                .loginPage("/api/v1/user/test/loginForm")
//                .loginProcessingUrl("/api/v1/")
//                .userInfoEndpoint()
//                .userService(service);
        http.addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
    }
}