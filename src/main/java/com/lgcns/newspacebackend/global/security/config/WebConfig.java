//package com.lgcns.newspacebackend.global.security.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//        		.allowedHeaders("Authorization", "Content-Type")
//                .allowedOrigins("http://localhost:8080","http://localhost:5173", "http://kudong.kr:55020", "http://kudong.kr:55021")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
//                .allowedHeaders("*")
//                .allowCredentials(true);
////        registry
////		        .addMapping("/api/user/login")
////		        .allowedOrigins("http://localhost:8080","http://localhost:5173", "http://kudong.kr:55020")
////		        .allowedMethods("POST");
//    }
//}
