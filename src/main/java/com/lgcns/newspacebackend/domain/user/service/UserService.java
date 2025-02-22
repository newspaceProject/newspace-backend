package com.lgcns.newspacebackend.domain.user.service;

import com.lgcns.newspacebackend.domain.user.dto.SignupRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoResponseDto;
import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.entity.UserRole;
import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto requestDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        log.info("[회원가입 요청] username: {}, name: {}, nickname: {}",
                requestDto.getUsername(), requestDto.getName(), requestDto.getNickname());

        // validation 유효성 검사
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // 비밀번호, 비밀번호 확인 일치 검사
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다.");
        }

        // 유저 등록
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .birth(requestDto.getBirth())
                .profileImage(null)
                .userRole(UserRole.USER)
                .build();

        log.info("[회원가입 성공] username: {}", user.getUsername());

        userRepository.save(user);
    }

    // 유저아이디 중복 체크
    @Transactional
    public boolean checkId(String username) {
        boolean isUserNameExist = userRepository.findByUsername(username).isPresent();

        if (!isUserNameExist) {
            return false;
        }
        return true;
    }

    // 회원정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

        UserInfoResponseDto userInfo = new UserInfoResponseDto(user);
        return userInfo;
    }

    // 회원정보 수정
    @Transactional
    public void updateUserInfo(Long userId, UserInfoRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

        // 닉네임 입력하면 닉네임 변경
        if (requestDto.getNickname() != null && !requestDto.getNewPassword().isEmpty()) {
            user.updateNickname(requestDto.getNickname());
        }

        // 비밀번호 변경하려고 입력했으면 비밀번호 변경
        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()
            && requestDto.getNewPasswordConfirm() != null && !requestDto.getNewPasswordConfirm().isEmpty()) {

            if (!requestDto.getNewPassword().equals(requestDto.getNewPasswordConfirm())) {
                throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다");
            }

            // 비밀번호 변경
            user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
        }

        userRepository.save(user);
    }
}
