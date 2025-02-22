package com.lgcns.newspacebackend.domain.user.controller;

import com.lgcns.newspacebackend.domain.user.dto.SignupRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoResponseDto;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto,
                                    BindingResult bindingResult) throws MethodArgumentNotValidException {
        userService.signup(requestDto, bindingResult);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam("username") String username) {
        boolean isValid = userService.checkId(username);

        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원정보 조회
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserInfo(userDetails.getUser().getId()));
    }

    // 회원정보 수정
    @PatchMapping("/info")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody UserInfoRequestDto requestDto) {
        userService.updateUserInfo(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.ok().build();
    }

}
