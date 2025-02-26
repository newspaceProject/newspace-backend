package com.lgcns.newspacebackend.domain.user.entity;

import com.lgcns.newspacebackend.global.entity.TimeStamp;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.AccessTokenInfo;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo.RefreshTokenInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends TimeStamp {
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
    private LocalDateTime accessTokenExpirationTime;

    @Column(length = 250)
    private String refreshToken;
    private LocalDateTime refreshTokenExpirationTime;

	public void setTokenExpirationTime(LocalDateTime now) {
        this.accessTokenExpirationTime = now;
        this.refreshTokenExpirationTime = now;
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
        this.accessTokenExpirationTime = convertToLocalDateTime(accessTokenInfo.getAccessTokenExpireTime());
	}

	public void updateRefreshTokenInfo(RefreshTokenInfo refreshTokenInfo) {
		this.refreshToken = refreshTokenInfo.getRefreshToken();
        this.refreshTokenExpirationTime = convertToLocalDateTime(refreshTokenInfo.getRefreshTokenExpireTime());
	}

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
