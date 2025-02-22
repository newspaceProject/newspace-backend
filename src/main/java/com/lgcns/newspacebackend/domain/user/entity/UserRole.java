package com.lgcns.newspacebackend.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRole{
    USER,
    ADMIN

//    USER(Authority.USER),s
//    ADMIN(Authority.ADMIN);
//
//    private final String authority;
//
//    UserRole(String authority) {
//        this.authority = authority;
//    }
//
//    public static class Authority {
//        public static final String ADMIN = "ROLE_ADMIN";
//        public static final String USER = "ROLE_USER";
//    }
}
