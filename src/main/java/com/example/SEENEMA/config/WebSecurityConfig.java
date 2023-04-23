package com.example.SEENEMA.config;

import com.example.SEENEMA.jwt.JwtAuthenticationFilter;
import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.user.service.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
//    private final PrincipalDetailsService service;

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
                .antMatchers(HttpMethod.POST, "/api/v1/theater-review/upload").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/theater-review/auth").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/theater-review/{postNo}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/theater-review/{postNo}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/theater-review/{postNo}/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/theater-review/{postNo}/{commentId}/auth").permitAll()
                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/api/v1/user/test/loginForm")
//                .loginProcessingUrl("/api/v1/user/test/login")
//                .defaultSuccessUrl("/api/v1/");
//                .and()
//                .oauth2Login()
//                .loginPage("/api/v1/user/test/loginForm")
//                .loginProcessingUrl("/api/v1/")
//                .userInfoEndpoint()
//                .userService(service);
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
    }
}