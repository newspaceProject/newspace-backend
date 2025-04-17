package com.lgcns.newspacebackend.global.security.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.lgcns.newspacebackend.global.exception.BaseException;
import com.lgcns.newspacebackend.global.exception.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.lgcns.newspacebackend.domain.user.entity.UserRole;
import com.lgcns.newspacebackend.global.security.constant.GrantType;
import com.lgcns.newspacebackend.global.security.constant.TokenType;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtTokenUtil은 JWT 토큰 생성, 검증 및 관리 기능을 제공합니다.
 */
@Slf4j
@Component
public class JwtTokenUtil {

	// 액세스 토큰, 리프레시 토큰 구분
	@Value("${spring.token.access-token-expiration-time}")
    private String accessTokenExpirationTime;
    @Value("${spring.token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;
    @Value("${spring.token.secret}")
    private String tokenSecret;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * 초기화 메서드로, JWT 서명을 위한 키를 설정합니다.
     */
    @PostConstruct
    public void init() {
    	// 인코딩 된 시크릿 복호화
        byte[] bytes = Base64.getDecoder().decode(tokenSecret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 요청의 쿠키에서 JWT 토큰을 추출합니다.
     *
     * @param httpServletRequest HTTP 요청
     * @return JWT 토큰
     */
    public String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) 
            {
            	try
				{
					log.info("[JwtTokenUtil] Raw cookie: {}", URLDecoder.decode(cookie.getValue(), "UTF-8"));
				}
				catch(UnsupportedEncodingException e1)
				{
					e1.printStackTrace();
				}
            	
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        String decodedToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        log.info("[JwtTokenUtil] decodedToken cookie: {}", decodedToken);
                        return decodedToken;
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * JWT 토큰에서 Bearer 타입을 제거합니다.
     *
     * @param token JWT 토큰
     * @return Bearer 타입이 제거된 토큰
     */
    public static String substringToken(String token) {
        if (StringUtils.hasText(token)) {
            if (token.startsWith(GrantType.BEARER.getType())) {
                String tokenSubstring = token.substring(7);
                return tokenSubstring;
            } else {
            	 throw new BaseException(BaseResponseStatus.INVALID_BEARER_GRANT_TYPE);
            }
        }
        throw new BaseException(BaseResponseStatus.TOKEN_INVALID);
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     *
     * @param token JWT 토큰
     * @return 유효한 경우 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            log.info("[validateToken] 검증 완료");
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new BaseException(BaseResponseStatus.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseStatus.TOKEN_EXPIRED);
        }
    }

    /**
     * JWT 토큰에서 클레임을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 클레임
     */
    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.TOKEN_INVALID);
        }
        log.info("[getTokenClaims] 클레임 추출");
        return claims;
    }

    /**
     * 액세스 토큰의 만료 시간을 생성합니다.
     *
     * @return 액세스 토큰 만료 시간
     */
    public Date createAccessTokenExpirationTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * 리프레시 토큰의 만료 시간을 생성합니다.
     *
     * @return 리프레시 토큰 만료 시간
     */
    public Date createRefreshTokenExpirationTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    /**
     * 액세스 토큰을 생성합니다.
     *
     * @param username 사용자 로그인 ID
     * @param expirationTime 만료 시간
     * @return 액세스 토큰
     */
    public String createAccessToken(String username, UserRole userRole, Date expirationTime) {
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("username", username)
                .claim("auth", userRole)
                .signWith(key, signatureAlgorithm)
                .setHeaderParam("typ", "JWT")
                .compact();
        return accessToken;
    }

    /**
     * 리프레시 토큰을 생성합니다.
     *
     * @param username 사용자 로그인 ID
     * @param expirationTime 만료 시간
     * @return 리프레시 토큰
     */
    public String createRefreshToken(String username, UserRole userRole, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("username", username)
                .claim("auth", userRole)
                .signWith(key, signatureAlgorithm)
                .setHeaderParam("typ", "JWT")
                .compact();
        return refreshToken;
    }

    /**
     * 액세스 토큰 정보를 생성합니다. (email 기반)
     *
     * @param username 사용자 로그인 ID
     * @return 액세스 토큰 정보
     */
    public JwtTokenInfo.AccessTokenInfo createAccessTokenInfo(String username, UserRole userRole) {
        String accessToken = createAccessToken(username, userRole, createAccessTokenExpirationTime());
        log.info("[createAccessTokenInfo] 액세스 토큰 발급");
        return JwtTokenInfo.AccessTokenInfo.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(createAccessTokenExpirationTime())
                .build();
    }

    /**
     * 리프레시 토큰 정보를 생성합니다. (email 기반)
     *
     * @param username 사용자 로그인 ID
     * @return 리프레시 토큰 정보
     */
    public JwtTokenInfo.RefreshTokenInfo createRefreshTokenInfo(String username, UserRole userRole) {
        String refreshToken = createRefreshToken(username, userRole, createRefreshTokenExpirationTime());
        log.info("[createAccessTokenInfo] 리프레시 토큰 발급");
        return JwtTokenInfo.RefreshTokenInfo.builder()
                .grantType(GrantType.BEARER.getType())
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(createRefreshTokenExpirationTime())
                .build();
    }

    /**
     * JWT 토큰을 쿠키에 추가합니다.
     *
     * @param token JWT 토큰
     * @return JWT 토큰이 포함된 쿠키
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public Cookie addTokenToCookie(String token) throws UnsupportedEncodingException {
        String encodedToken = URLEncoder.encode(BEARER_PREFIX + token, "utf-8").replaceAll("\\+", "%20");
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, encodedToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTP로 가능하게끔 설정
        log.info("[addTokenToCookie] 쿠키 내 JWT 토큰 추가");
        return cookie;
    }

    /**
     * 쿠키에서 JWT 토큰을 삭제합니다.
     *
     * @return 삭제된 JWT 토큰이 포함된 쿠키
     */
    public Cookie removeTokenCookie() {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, "");
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        log.info("[removeTokenCookie] 쿠키 내 JWT 토큰 삭제");
        return cookie;
    }
}
