package com.lgcns.newspacebackend.domain.user.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
