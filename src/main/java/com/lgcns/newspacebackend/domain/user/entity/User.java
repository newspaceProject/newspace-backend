package com.lgcns.newspacebackend.domain.user.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.AccessTokenInfo;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.RefreshTokenInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true , nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(length = 250)
    private String accessToken;
    private Date accessTokenExpirationTime;

    @Column(length = 250)
    private String refreshToken;
    private Date refreshTokenExpirationTime;
    
	public void updateAccessTokenInfo(AccessTokenInfo accessTokenInfo) {
		this.accessToken = accessTokenInfo.getAccessToken();
		this.accessTokenExpirationTime = accessTokenInfo.getAccessTokenExpireTime();
	}

	public void updateRefreshTokenInfo(RefreshTokenInfo refreshTokenInfo) {
		this.refreshToken = refreshTokenInfo.getRefreshToken();
		this.refreshTokenExpirationTime = refreshTokenInfo.getRefreshTokenExpireTime();
	}
    
}