package com.lgcns.newspacebackend.global.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.entity.UserRole;
import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsServiceImpl;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 요청에서 토큰 추출
        String token = jwtTokenUtil.getTokenFromRequest(request);
        // * 토큰이 존재한다면
        if (StringUtils.hasText(token)) {
	        // 토큰 페이로드에서 본문만 분리
	        String tokenSubstring = jwtTokenUtil.substringToken(token);
        	try {
			        // 분리된 토큰의 유효성 검토
	        		// * 유효한 액세스 토큰이면 클레임 정보 추출 로직 수행
			        if (jwtTokenUtil.validateToken(tokenSubstring)) {
			        	Claims claims = jwtTokenUtil.getTokenClaims(tokenSubstring);
			        	String username = (String) claims.get("username");
			        	
			        	if(username == null) {
			        		log.error("[doFilterInternal] 클레임에서 아이디를 추출하지 못했습니다.");
	                        filterChain.doFilter(request, response);
	                        return;
			        	}
			        	// 인증정보를 가져오는 메서드를 하단에 만들었다. 
			        	setAuthentication(username);
			        	// 필터체인으로 요청과 응답을 보내고 리턴
			        	filterChain.doFilter(request, response);
			        	return;
			        }
		        } catch(Exception e) {
		        	log.error("[doFilterInternal] 토큰 검증 오류 발생", e);
		        	
		        	// * 액세스 토큰 만료 시
	                User user = userService.findUserByAccessToken(tokenSubstring);
	                if (user == null) {
	                    log.error("[doFilterInternal] AccessToken으로 유저를 찾지 못했습니다.");
	                    filterChain.doFilter(request, response);
	                    return;
	                }

	                // * 리프레시 토큰이 유효한 경우
	                log.info("[doFilterInternal] 액세스 토큰 재발급 시도");

	                // * Refresh 토큰을 가져와서 유효성 확인을 위한 로직들
	                String refreshToken = user.getRefreshToken();
	                String username = user.getUsername();
	                UserRole userRole = user.getUserRole();
	                
	                // * 리프레시 토큰 유효기간이 남았을 경우
	                if (userService.isRefreshTokenValid(refreshToken)) {
	                    JwtTokenInfo.AccessTokenInfo newAccessTokenInfo = jwtTokenUtil.createAccessTokenInfo(username, userRole);
	                    Cookie jwtCookie = jwtTokenUtil.addTokenToCookie(newAccessTokenInfo.getAccessToken());
	                    response.addCookie(jwtCookie);

	                    userService.updateAccessToken(user, newAccessTokenInfo);
	                    log.info("[doFilterInternal] 액세스 토큰 재발급 완료");

	                    setAuthentication(username);
	                    filterChain.doFilter(request, response);
	                    return;

	                } else {
	                    // * 리프레시 토큰이 만료된 경우
	                    log.info("[doFilterInternal] 리프레시 토큰 만료");

	                    Cookie removedTokenCookie = jwtTokenUtil.removeTokenCookie();
	                    response.addCookie(removedTokenCookie);

//	                    // Filter 응답 처리를 위한 유틸 클래스 필요
//	                    FilterResponseUtil.sendFilterResponse(response,
//	                            HttpServletResponse.SC_UNAUTHORIZED,
//	                            BaseResponseStatus.REFRESH_TOKEN_EXPIRED);
	                }
	            }
	        } else {
	            // 1-2. 액세스 토큰이 없는 경우 필터 체인 진행
	            filterChain.doFilter(request, response);
	        }
	    }
    /**
     * 아이디 기반으로 유저 인증 및 보안 컨텍스트 설정을 수행합니다.
     *
     * @param username 유저 아이디
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 아이디를 기반으로 유저 인증결과를 객체로 저장함 
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
    /**
     *  유저아이디의 인증용 객체를 생성하는 메서드
     *  
     *  @param username 유저 아이디
     *  @return 인증을 위해 생성된 객체
     */
    public Authentication createAuthentication(String username) {
    	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    	// 2번째 매개변수는 비밀번호와 관련된 credential 이다 민감한 정보인 비밀번호를 token 에 담을 이유가 없기에 null 이 들어왔다.
    	return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
    }
    
    
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        log.debug("shouldNotFilter >>>>>>>>>>");
//        
//        String[] excludePath = { "/login", "/joinProc" };
//       
//        
//        String uri = request.getRequestURI();
//        boolean result = Arrays.stream(excludePath).anyMatch(uri::startsWith);
//        log.debug(">>>" + result);
//        
//        return result;
//    }
    
}