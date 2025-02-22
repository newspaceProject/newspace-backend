package com.lgcns.newspacebackend.global.security.constant;
import lombok.Getter;

// 토큰의 타입을 권한 타입을 가져오기 위한 구분자
@Getter
public enum GrantType {

    BEARER("Bearer");

    GrantType(String type) {
        this.type = type;
    }

    private String type;
}