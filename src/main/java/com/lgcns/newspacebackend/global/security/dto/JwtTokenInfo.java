package com.lgcns.newspacebackend.global.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
public class JwtTokenInfo {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessTokenInfo {
    	// Bearer 페이로드 타입변수
        private String grantType;
        private String accessToken;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenInfo {
    	// Bearer 페이로드 타입변수
        private String grantType;
        private String refreshToken;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;
    }
}
