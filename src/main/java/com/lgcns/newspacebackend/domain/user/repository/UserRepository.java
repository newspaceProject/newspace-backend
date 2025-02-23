package com.lgcns.newspacebackend.domain.user.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lgcns.newspacebackend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
  
    Optional<User> findUserByAccessToken(String accessToken);

	@Query("SELECT u.refreshTokenExpirationTime FROM User u WHERE u.refreshToken = :refreshToken")
	Date findRefreshTokenExpirationTimeByRefreshToken(@Param("refreshToken") String refreshToken);

	
}
