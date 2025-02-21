package com.lgcns.newspacebackend.global.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.entity.UserRole;

public class UserDetailsImpl implements UserDetails{
	private User userEntity;
	
	public UserDetailsImpl(User userEntity) {
		this.userEntity = userEntity;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		// enum 타입으로 생성된 UserRole 권한을 
		// 가져오고 String으로 변환
        String auth = String.valueOf(userEntity.getUserRole());
        // granted 권한 컬랙션에 데이터 추가
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		// ADMIN이자 USER로써의 권한 같은
		// 여러종류의 권한을 부여하기 위해
		// simpleGranted를 추가하는 것이다.
		grantedAuthorities.add(simpleGrantedAuthority);
		return grantedAuthorities;
	}
	
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }
    
}
