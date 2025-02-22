package com.lgcns.newspacebackend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime accessTokenExpirationTime;

    @Column(length = 250)
    private String refreshToken;
    private LocalDateTime refreshTokenExpirationTime;
}
