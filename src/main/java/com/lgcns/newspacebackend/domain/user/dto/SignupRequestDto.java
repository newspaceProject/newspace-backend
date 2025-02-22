package com.lgcns.newspacebackend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "사용자 아이디를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "아이디는 4~20글자의 영어 대소문자, 숫자, _만 허용됩니다")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^.{4,}$", message = "비밀번호는 4글자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력하세요")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력하세요")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "이름은 한글, 영대문자로만 입력이 허용됩니다")
    private String name;

    @NotBlank(message = "닉네임을 입력하세요")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "닉네임은 한글, 영대문자로만 입력이 허용됩니다")
    private String nickname;

    @NotBlank(message = "생년월일을 입력하세요")
    private String birth;
}
