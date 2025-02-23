package com.lgcns.newspacebackend.domain.user.entity;

import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.AccessTokenInfo;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.RefreshTokenInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true , nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(length = 250)
    private String accessToken;
    private Date accessTokenExpirationTime;

    @Column(length = 250)
    private String refreshToken;
    private Date refreshTokenExpirationTime;

	public void setTokenExpirationTime(LocalDateTime now) {
        // LocalDateTime을 ZonedDateTime으로 변환
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());

        // ZonedDateTime을 Date로 변환
        this.accessTokenExpirationTime = Date.from(zonedDateTime.toInstant());
        this.refreshTokenExpirationTime = Date.from(zonedDateTime.toInstant());
	};
    
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	};

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
    
	public void updateAccessTokenInfo(AccessTokenInfo accessTokenInfo) {
		this.accessToken = accessTokenInfo.getAccessToken();
		this.accessTokenExpirationTime = accessTokenInfo.getAccessTokenExpireTime();
	}

	public void updateRefreshTokenInfo(RefreshTokenInfo refreshTokenInfo) {
		this.refreshToken = refreshTokenInfo.getRefreshToken();
		this.refreshTokenExpirationTime = refreshTokenInfo.getRefreshTokenExpireTime();
	}

}
