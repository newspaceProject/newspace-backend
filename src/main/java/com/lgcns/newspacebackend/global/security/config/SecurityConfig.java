package com.lgcns.newspacebackend.global.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsServiceImpl;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthenticationFilter;
import com.lgcns.newspacebackend.global.security.filter.JwtAuthorizationFilter;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
public class SecurityConfig{


    private UserDetailsServiceImpl userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationConfiguration authenticationConfiguration;
    private UserRepository userRepository;
    // 만들어야함
    private UserService userService;

    // 인증 매니저
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
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
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/news/**").permitAll() // 인증 없이 허용
                .requestMatchers("/api/user/**").permitAll() // 인증 없이 허용
                .requestMatchers("/api/**").permitAll() // 인증 없이 허용                
//                .requestMatchers("/api/notice/**").hasRole("ADMIN") // ADMIN만 접근 가능
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 X
            .formLogin(form -> form.disable()) // 폼 로그인 비활성화
            .logout(logout -> logout.disable()); // 로그아웃 비활성화

        // JWT 필터 추가 (filter 처리 순서 오류 발생)
        // 분석 꼭 해라...
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        // CORS 설정
//        http.cors(Customizer.withDefaults());
      
        return http.build();
    }
}
