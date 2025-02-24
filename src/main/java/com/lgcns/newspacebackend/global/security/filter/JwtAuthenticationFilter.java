package com.lgcns.newspacebackend.global.security.filter;

import java.io.IOException;

import com.lgcns.newspacebackend.global.security.util.FilterResponseUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.newspacebackend.domain.user.dto.LoginRequestDto;
import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.entity.UserRole;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    // 사용자의 Access 토큰을 업데이트 관리할 로직
    private final UserService userService;
    private FilterChain chain;

    // 인증단계에서는 DetailService(권한, 아디, 비번)등을 다룰 필요 없음
    // api login 으로 들어온 인증 요청은 생성자로써 들어오고 인증 매니저가 처리 
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        setFilterProcessesUrl("/api/user/login"); // 로그인 처리 URL 설정
    }

	// 일반 로그인 시도 처리
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("[attemptAuthentication] 일반 로그인 시도");
    	try {
    		// 로그인 데이터 불러오기
    		LoginRequestDto loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
	        String username  = loginRequestDto.getUsername();
	        String password = loginRequestDto.getPassword();
	
	        // 인증 토큰 생성 ＝＞ authorizationFilter 로 ID와 Password 를 전달하는 역할
	        UsernamePasswordAuthenticationToken authenticationToken
	        	= new UsernamePasswordAuthenticationToken(username, password, null);
	        	// 권한에 대한 정보는 필터 자체를 나눠서 null이 들어감
	        	
	        // AuthenticationManager를 사용하여 인증 수행
	        return getAuthenticationManager().authenticate(authenticationToken);
    	}catch(IOException e) {
            // 로그인 데이터 읽기 실패 시 예외 던지기
            throw new RuntimeException(e.getMessage());	
    	}
    }
    
    // 일반 로그인 성공시
    @Override
     protected void successfulAuthentication(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
     	this.chain = chain;
     	
     	// 인증 사용자 정보 가져오기
     	User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
     	String username = user.getUsername();
     	UserRole userRole = user.getUserRole();
     	
     	// 액세스 토큰과 리프레시 토큰 정보를 생성
        JwtTokenInfo.AccessTokenInfo accessTokenInfo = jwtTokenUtil.createAccessTokenInfo(username, userRole);
        JwtTokenInfo.RefreshTokenInfo refreshTokenInfo = jwtTokenUtil.createRefreshTokenInfo(username, userRole);
        
        // JWT 토큰을 쿠키에 추가
        Cookie jwtCookie = jwtTokenUtil.addTokenToCookie(accessTokenInfo.getAccessToken());
        httpServletResponse.addCookie(jwtCookie);

        // 사용자 서비스에서 토큰 정보 업데이트
        userService.updateAccessToken(user, accessTokenInfo);
        userService.updateRefreshToken(user, refreshTokenInfo);

        
        // 예외처리로직을 직접 작성할 것이 아니기에 Filter 상의 응답이 필요하지 않다.
//        // 성공 응답 전송
//        FilterResponseUtil.sendFilterResponse(httpServletResponse,
//                HttpServletResponse.SC_OK,
//                BaseResponseStatus.LOGIN_SUCCESS);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse,
                                              AuthenticationException failed) throws IOException {
        log.info("[unsuccessfulAuthentication] 일반 로그인 실패");

        // 실패 응답 전송
        FilterResponseUtil.sendFilterResponse(httpServletResponse,
                HttpServletResponse.SC_UNAUTHORIZED);
    }
    
}