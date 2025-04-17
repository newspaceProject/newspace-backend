package com.lgcns.newspacebackend.global.security.config;

import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsServiceImpl;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthenticationFilter;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthorizationFilter;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final UserService userService;
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // jwtAuthor filter 빈 생성자
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtTokenUtil, userDetailsService, userService);
    }

    // jwtAuthenticate filter 빈 생성자
    // 유저 레포지토리에서 데이터를 받아와서 사용자 인증, 로그인 성공 실패 로직을 다룬다.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserRepository userRepository)  throws Exception {
    	JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenUtil,userService);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.securityMatchers(matchers ->
                matchers.requestMatchers(CorsUtils::isPreFlightRequest)
        ).authorizeHttpRequests(auth ->
                //auth.anyRequest().permitAll()
		        auth.requestMatchers("/api/user/login").permitAll()
		        .anyRequest().authenticated()
        );

        // JWT 인증 및 인가 필터 추가
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilter(jwtAuthenticationFilter(userRepository));
        http.cors(Customizer.withDefaults());

        return http.build();
    }
}
