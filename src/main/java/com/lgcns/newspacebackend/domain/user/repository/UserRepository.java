package com.lgcns.newspacebackend.domain.user.repository;

import com.lgcns.newspacebackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
  
    Optional<User> findUserByAccessToken(String accessToken);

    @Query("SELECT u.refreshTokenExpirationTime FROM User u WHERE u.refreshToken = :refreshToken")
    LocalDateTime findRefreshTokenExpirationTimeByRefreshToken(@Param("refreshToken") String refreshToken);

	
}
