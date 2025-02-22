package com.lgcns.newspacebackend.domain.user.controller;

import com.lgcns.newspacebackend.domain.user.dto.SignupRequestDto;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    // 회원가입
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto) {
//
//    }

    // 아이디 중복 체크

    // 개인정보 조회

    // 개인정보 수정
}
