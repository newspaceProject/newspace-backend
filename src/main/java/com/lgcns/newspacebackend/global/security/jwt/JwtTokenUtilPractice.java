//package com.lgcns.newspacebackend.global.security.jwt;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//import javax.crypto.SecretKey;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.data.repository.query.Param;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import com.lgcns.newspacebackend.domain.user.entity.User;
//import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.*;
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//
//// 롬복 로깅
//@Slf4j
//@Component
//public class JwtTokenUtilBackUp {
//	
//	// yml에서 작성한 암호화를 위한 토큰 key secret
//	@Value("${spring.token.secret}")
//    private String hmacKey;
//	// 토큰 유효기간
//	@Value("${spring.token.expiration-time}")
//    private Long expirationTime;
//    
//	// 토큰 암호화에 사용될 키
//    private Key key;
//    
//    // JWT 초기 키 (시크릿이 암호화 된상태라면 복호화해줘야함)
//    // secret value 값을 키로써 유지시키기 위한 장치
//    @PostConstruct
//    public void init() {
////    	 시크릿 암호를 암호화하지 않았기에 decode 할 필요 없이
////    	 바로 키로 넣어줬다.
//    	try {
//    		// 일반 문자열로 된 secret이기에 decode 로직을 쓸 필요 없다
//	    	byte[] decodedSecret = hmacKey.getBytes(StandardCharsets.UTF_8);
//	//    	 랜덤 암호화 시크릿
//	    	key = Keys.hmacShaKeyFor(decodedSecret);
//    	} catch (IllegalArgumentException e) {
//            log.error("Invalid Base64 encoded secret key: {}", hmacKey, e);
//            throw new RuntimeException("Invalid Base64 encoded secret key", e);
//        }
//    }
//    
//    // 시그니처 암호화를 위한 해시코드
////    private final SignatureAlgorithm signatureHashcode = SignatureAlgorithm.HS256;
//    
//    // 쿠키의 이름으로 정해질 부분 Authorization 이름으로 토큰이 전달됨
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    // 쿠키의 페이로드의 시작점을 설정해주자 Bearer ~token 페이로드~
//    public static final String BEARER_PREFIX = "Bearer ";
//    
//    
//    // JWT 액세스 토큰 생성 (UD에서의 username을 넣을 예정)
//    public String generateAccessToken(UserDetailsImpl userDetails) {
//    	Date now = new Date();
//        
//        String accessToken = Jwts.builder()	
//                .claim("username", userDetails.getUsername())
//                .claim("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .subject(userDetails.getUsername())
//                .id(String.valueOf(userDetails.hashCode()))
//                .issuedAt(now)
//                .expiration(new Date(now.getTime() + this.expirationTime))
//                // 시그니처를 암호화하기 위하여 
//                // Newspace's token secret 를 secret키로써 함께 보냄
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//        log.debug(accessToken);
//        
//        return accessToken;
//    }
//
//    // 토큰 유효성 검사 로직용 메서드 - 페이로드 추출 / 유효기간 추출 / 유효기간 체크 / 주요(sub) 클레임 추출 / 토큰 유효성 체킹
//    // 토큰에서 정보추출
//    private Claims getAllClaimsFromToken(String token) {
//        Jws<Claims> jwt = Jwts.parser()
//            .verifyWith((SecretKey) key)
//            .build()
//            .parseSignedClaims(token);
//        return jwt.getPayload();
//    }
//    
//    private Date getExpirationDateFromToken(String token) {
//        Claims claims = getAllClaimsFromToken(token);
//        return claims.getExpiration();
//    }
//    // jwt 만료 확인
//    private boolean isTokenExpired(String token) {
//        Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }    
//    
//    public String getSubjectFromToken(String token) {
//        Claims claims = getAllClaimsFromToken(token);
//        return claims.getSubject();
//    }
//    // 유효성 검사결과 반환 로직
//    public boolean validateToken(String token, User userEntity) {
//        // 토큰 유효기간 체크
//        if (isTokenExpired(token)) { return false; }
//        // 토큰 내용을 검증
//        String subject = getSubjectFromToken(token);
//        String username = userEntity.getUsername();
//        
//        // 회원으로써 존재하는 유저인지만 체크하면 되는가?
//        // 어드민 권한을 토큰으로 확인할 필요는 없겠죠..?
//        
//        // 토큰 추출정보와, 유저아이디를 비교한 결과값을 반환한다.
//        return subject != null && username != null && subject.equals(username);        
//    }
//
//    // JWT 토큰을 쿠키에 담기
//    public Cookie setTokenInCookie(String token) throws UnsupportedEncodingException {
//    	// String 형태의 토큰을 공백요소 제거후 
//    	// 암호화 해서 쿠키에 넣자
//    	String encodedToken = URLEncoder.encode(BEARER_PREFIX + token, "UTF-8").replaceAll("\\+", "%20");
//    	Cookie cookie = new Cookie(AUTHORIZATION_HEADER, encodedToken);
//        cookie.setPath("/"); // 쿠키 경로 설정
//        // js에서 접근 불가 csrf 보안요소 세팅
//        cookie.setHttpOnly(true);
//        // https를 사용하면 setSecure(true)
////        cookie.setSecure(true);
//        // 쿠키의 유효기간 설정
//        // 쿠키와 토큰의 인증 시간차이로 오차생기면 안됨
//        // 하루로 세팅
//        cookie.setMaxAge(86400000);
//        return cookie;
//    }
//    // 쿠키에서 JWT 토큰 추출
//    public String getTokenFromRequest(HttpServletRequest httpServletRequest) {
//    	// 받아온 쿠키를 http 요청으로 불러오고, 쿠키에 데이터가 있을때
//    	// Authorization 이름의 쿠키를 디코딩한다.
//        Cookie[] cookies = httpServletRequest.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//            	// 쿠키의 목록중 Authorization 이름을 가졌으면 디코딩 수행
//                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
//                    try {
//                        String decodedToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
//                        // Bearer 접두사 제거
//                        decodedToken = decodedToken.substring(BEARER_PREFIX.length());
//                        // 토큰값만 리턴
//                        return decodedToken;
//                    } catch (UnsupportedEncodingException e) {
//                    	// 디코딩 실패하면 보여줄 에러 로그
//                    	log.error("Failed to decode token from cookie: " + e.getMessage());
//                        return null;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//    
//    
//     
//}
