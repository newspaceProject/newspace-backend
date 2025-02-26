package com.lgcns.newspacebackend.global.exception;

import org.springframework.http.HttpStatus;

public enum BaseResponseStatus {

    LOGIN_SUCCESS(true, 2001, "로그인에 성공했습니다."),

    USER_NOT_FOUND(false, 4101, "유저 정보가 없습니다."),
    USERNAME_ALREADY_EXISTS(false, 4102, "중복된 ID 입니다."),
    INVALID_SIGNUP_DATA(false, 4103, "회원가입에 필요한 데이터를 찾을 수 없습니다."),
    PASSWORD_CONFIRM_MISMATCH(false, 4104, "비밀번호와 비밀번호 확인 값이 일치하지 않습니다."),
    NEW_PASSWORD_CONFIRM_MISMATCH(false, 4105, "새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다."),

    NOT_FOUND_CATEGORY(false, 4106, "해당 카테고리 id에 맞는 카테고리를 찾을 수 없습니다."),
    NOT_FOUND_KEYWORD(false, 4107, "해당 카테고리 id에 맞는 키워드를 찾을 수 없습니다."),
    NOT_FOUND_NOTICE(false, 4108, "해당 공지사항 id에 맞는 공지사항을 찾을 수 없습니다."),

    LOGIN_FAILED(false, 4201, "[unsuccessfulAuthentication] 로그인에 실패했습니다."),

    INVALID_BEARER_GRANT_TYPE(false, 4202, "Bearer 타입이 아닙니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND(false, 4203, "JWT 토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(false, 4204, "JWT 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(false, 4205, "JWT 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(false, 4206, "로그인 인증이 만료되었습니다. 다시 로그인을 진행해 주세요."),
    AUTHENTICATION_FAILED(false, 4207, "인증에 실패했습니다"),
    NOT_FOUND_USERDETAILS(false, 4208, "UserDetails의 유저의 정보를 찾을 수 없습니다."),

    FILE_NOT_FOUND(false, 4113, "파일을 찾을 수 없습니다");

    private final boolean isSuccess;
    private final int code;
    private final String message;
    private HttpStatus httpStatus;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    BaseResponseStatus(boolean isSuccess, int code, String message, HttpStatus httpStatus) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
