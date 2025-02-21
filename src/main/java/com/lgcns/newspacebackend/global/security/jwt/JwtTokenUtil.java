package com.lgcns.newspacebackend.global.security.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;

import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// 롬복 로깅
@Slf4j
public class JwtTokenUtil {
	
	// yml에서 작성한 암호화를 위한 토큰 key secret
	@Value("${spring.token.secret}")
    private String hmacKey;
	// 토큰 유효기간
	@Value("${spring.token.expiration-time}")
    private Long expirationTime;
    
	// 토큰 암호화에 사용될 키
//    private Key key;
    
    // JWT 초기 키 (시크릿이 암호화 된상태라면 복호화해줘야함)
    @PostConstruct
    public void init() {
    	// 시크릿 암호를 암호화하지 않았기에 decode 할 필요 없이
    	// 바로 키로 넣어줬다.
//    	byte[] decodedSecret = Base64.getDecoder().decode(hmacKey);
    	// 랜덤 암호화 시크릿
//    	key = Keys.hmacShaKeyFor(decodedSecret);
    }
    
    // 시그니처 암호화를 위한 해시코드
//    private final SignatureAlgorithm signatureHashcode = SignatureAlgorithm.HS256;
    
    // 쿠키의 이름으로 정해질 부분 Authorization 이름으로 토큰이 전달됨
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 쿠키의 페이로드의 시작점을 설정해주자 Bearer ~token 페이로드~
    public static final String BEARER_PREFIX = "Bearer ";
    
    
    // JWT 액세스 토큰 생성 (UD에서의 username을 넣을 예정)
    public String generateAccessToken(UserDetailsImpl userDetails) {
    	Date now = new Date();
        
        String accessToken = Jwts.builder()	
                .claim("name", userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .subject(userDetails.getUsername())
                .id(String.valueOf(userDetails.hashCode()))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + this.expirationTime))
                // 시그니처를 암호화하기 위하여 
                // Newspace's token secret 를 secret키로써 함께 보냄
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();
        log.debug(accessToken);
        
        return accessToken;
    }
    // 토큰에서 정보추출
        
    // JWT 쿠키에 담아 보내기
//    Cookie cookie = new Cookie(AUTHORIZATION_HEADER, );
    
    // 쿠키에서 JWT 토큰 추출
    public String getTokenFromRequest(HttpServletRequest httpServletRequest) {
    	// 받아온 쿠키를 http 요청으로 불러오고, 쿠키에 데이터가 있을때
    	// Authorization 이름의 쿠키를 디코딩한다.
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        String decodedToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        return decodedToken;
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    //
    
    
     
}