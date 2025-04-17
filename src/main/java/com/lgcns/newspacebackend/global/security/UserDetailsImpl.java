package com.lgcns.newspacebackend.global.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lgcns.newspacebackend.domain.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDetailsImpl implements UserDetails{
	private final User user;
	
	public UserDetailsImpl(User userEntity) {
	    if (userEntity == null) log.error("User is null in UserDetailsImpl constructor");
		this.user = userEntity;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		// enum 타입으로 생성된 UserRole 권한을 
		// 가져오고 String으로 변환
        String auth = String.valueOf(user.getUserRole());
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
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
	public User getUser() {
		return user;
	}

    
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는지 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겨있지 않은지 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않았는지 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되어 있는지 여부
    }
}
