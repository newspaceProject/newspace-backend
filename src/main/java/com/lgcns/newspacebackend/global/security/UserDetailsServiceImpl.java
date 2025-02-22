package com.lgcns.newspacebackend.global.security;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    
    // 유저 이름으로 레포지토리에서 찾아내는 클래스
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
        User userEntity = userRepository.findByUsername(username).orElseThrow(()-> new IllegalIdentifierException("user is not exist"));
        if (userEntity == null) {
            throw new UsernameNotFoundException("등록된 사용자가 없습니다.");
        }
        return new UserDetailsImpl(userEntity);
    }
}
