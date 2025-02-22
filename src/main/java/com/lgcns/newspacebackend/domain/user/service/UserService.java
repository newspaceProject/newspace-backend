package com.lgcns.newspacebackend.domain.user.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo;

@Service
public class UserService {
	
	UserRepository userRepository;
	
	/**
     * 액세스 토큰 정보 업데이트
     *
     * @param user 토큰 정보를 업데이트할 유저 객체
     * @param accessTokenInfo 업데이트할 액세스 토큰 정보
     */
    @Transactional
    public void updateAccessToken(User user, JwtTokenInfo.AccessTokenInfo accessTokenInfo) {
        // 액세스 토큰 정보 업데이트
        user.updateAccessTokenInfo(accessTokenInfo);
        // 변경 사항 DB에 저장
        userRepository.save(user);
    }

    /**
     * 리프레시 토큰 정보 업데이트
     * 인증 / 인가에서 사용
     * @param user 토큰 정보를 업데이트할 유저 객체
     * @param refreshTokenInfo 업데이트할 리프레시 토큰 정보
     */
    @Transactional
    public void updateRefreshToken(User user, JwtTokenInfo.RefreshTokenInfo refreshTokenInfo) {
        // 리프레시 토큰 정보 업데이트
        user.updateRefreshTokenInfo(refreshTokenInfo);
        // 변경 사항 DB에 저장
        userRepository.save(user);
    }

    /**
     * 액세스 토큰 기반 유저 확인
     *
     * @param accessToken 유저의 액세스 토큰
     * @return 주어진 액세스 토큰을 가진 유저
     * @throws BaseException 주어진 액세스 토큰을 가진 유저가 없을 경우
     */
    @Transactional(readOnly = true)
    public User findUserByAccessToken(String accessToken) {
        // 액세스 토큰으로 유저 찾기
        return userRepository.findUserByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Can't find Token"));
    }

    /**
     * 리프레시 토큰 유효 여부 확인
     * 
     * @param refreshToken 유효 여부를 확인할 리프레시 토큰
     * @return 리프레시 토큰의 유효 여부(유효한 경우 true, 만료된 경우 false)
     */
    @Transactional(readOnly = true)
    public boolean isRefreshTokenValid(String refreshToken) {
        // 리프레시 토큰의 만료 시간 확인
//        LocalDateTime refreshTokenExpirationTime = userRepository
//                .findRefreshTokenExpirationTimeByRefreshToken(refreshToken);
        // 리프레시 토큰의 만료 시간 확인
        Date refreshTokenExpirationDate = userRepository.findRefreshTokenExpirationTimeByRefreshToken(refreshToken);
        
        // Date를 LocalDateTime으로 변환
        LocalDateTime refreshTokenExpirationTime = refreshTokenExpirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // 현재 시간과 비교하여 유효 여부 반환
        return !refreshTokenExpirationTime.isBefore(LocalDateTime.now());
    }
}
