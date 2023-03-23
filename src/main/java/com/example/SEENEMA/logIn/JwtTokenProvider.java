package com.example.SEENEMA.logIn;

import com.example.SEENEMA.test.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    // 토큰 생성, 인증, 권한부여, 유효성 검사, PK추출 기능 등
    // filterCLASS에서 사전 검증 거침
    @Value("spring.jwt.secret")
    private String secretKey;

    // 토큰 유효시간(60*60*1000L=1hour)
    private long tokenValidTime = 60 * 60 * 1000L;
    @Autowired
    private final UserServiceImpl service;

    // 초기화, secretKey를 Base64로 인코딩
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Token create
    public String createToken(String userPK, List<String> roles){
        // JWT -> header.payload.signature
        // JWT payload에 저장된 정보단위, user를 식별하는 값 넣음.
        Claims claims = Jwts.claims().setSubject(userPK);
        claims.put("roles",roles);  // (key,value)
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)  // Data
                .setIssuedAt(now)   // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 기간, 지금부터 tokenValidTime동안
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘, secret 값
                .compact(); // Token create
    }

    // Token으로 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = service.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    // Token에서 UserPK 추출
    public String getUserPK(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // request Header에서 token 가져오기
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization");
        // return requset.getHeader("X-AUTH-TOKEN");
    }

    // Token 유효성, 만료기간 확인
    public boolean validateToken(String jwttoken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwttoken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch(Exception e){
            return false;
        }
    }
}
