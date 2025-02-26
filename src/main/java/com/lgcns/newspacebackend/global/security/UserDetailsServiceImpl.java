package com.lgcns.newspacebackend.global.security;

import com.lgcns.newspacebackend.global.exception.BaseException;
import com.lgcns.newspacebackend.global.exception.BaseResponseStatus;
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
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return new UserDetailsImpl(user);
    }
}
