package com.lgcns.newspacebackend.global.security.config;

import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsServiceImpl;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthenticationFilter;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthorizationFilter;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
	
    
    // 
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/news/**").permitAll() // 인증 없이 허용
//                .requestMatchers("/api/user/**").permitAll() // 인증 없이 허용
//                .requestMatchers("/api/**").permitAll() // 인증 없이 허용                
////                .requestMatchers("/api/notice/**").hasRole("ADMIN") // ADMIN만 접근 가능
//                .anyRequest().permitAll() // 그 외 모든 요청은 인증 필요
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 X
//            .formLogin(form -> form.disable()) // 폼 로그인 비활성화
//            .logout(logout -> logout.disable()); // 로그아웃 비활성화
//
//        // JWT 필터 추가 (filter 처리 순서 오류 발생)
//        // 분석 꼭 해라...
//        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class);
////        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        
//        // CORS 설정
//        http.cors(Customizer.withDefaults());
//      
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .anyRequest().permitAll()
        );

        // JWT 인증 및 인가 필터 추가
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class);          

        return http.build();
    }
}
