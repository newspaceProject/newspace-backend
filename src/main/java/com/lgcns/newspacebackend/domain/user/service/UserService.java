package com.lgcns.newspacebackend.domain.user.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.lgcns.newspacebackend.domain.user.dto.SignupRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoResponseDto;
import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.domain.user.entity.UserRole;
import com.lgcns.newspacebackend.domain.user.repository.UserRepository;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
import com.lgcns.newspacebackend.global.security.dto.JwtTokenInfo;
import com.lgcns.newspacebackend.global.security.jwt.JwtTokenUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService
{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenUtil jwtTokenUtil;

	// 회원가입
	@Transactional
	public void signup(SignupRequestDto requestDto, BindingResult bindingResult) throws MethodArgumentNotValidException
	{
		log.info("[회원가입 요청] username: {}, name: {}, nickname: {}", requestDto.getUsername(), requestDto.getName(),
				requestDto.getNickname());

		// validation 유효성 검사
		if(bindingResult.hasErrors())
		{
			throw new MethodArgumentNotValidException(null, bindingResult);
		}

		// 비밀번호, 비밀번호 확인 일치 검사
		if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm()))
		{
			throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다.");
		}

		// 유저 등록
		User user = User.builder().username(requestDto.getUsername())
				.password(passwordEncoder.encode(requestDto.getPassword())).name(requestDto.getName())
				.nickname(requestDto.getNickname()).birth(requestDto.getBirth()).profileImage("")
				.userRole(UserRole.USER).build();

		log.info("[회원가입 성공] username: {}", user.getUsername());

		userRepository.save(user);
	}

	// 유저아이디 중복 체크
	@Transactional
	public boolean checkId(String username)
	{
		boolean isUserNameExist = userRepository.findByUsername(username).isPresent();

		if(!isUserNameExist)
		{
			return false;
		}
		return true;
	}

	// 회원정보 조회
	@Transactional(readOnly = true)
	public UserInfoResponseDto getUserInfo(Long userId)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

		UserInfoResponseDto userInfo = new UserInfoResponseDto(user);
		return userInfo;
	}
	
	// 회원정보 수정
	@Transactional
	public void updateUserInfo(Long userId, UserInfoRequestDto requestDto)
	{
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

		// 닉네임 입력하면 닉네임 변경
		if(requestDto.getNickname() != null && !requestDto.getNewPassword().isEmpty())
		{
			user.updateNickname(requestDto.getNickname());
		}

		// 비밀번호 변경하려고 입력했으면 비밀번호 변경
		if(requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()
				&& requestDto.getNewPasswordConfirm() != null && !requestDto.getNewPasswordConfirm().isEmpty())
		{

			if(!requestDto.getNewPassword().equals(requestDto.getNewPasswordConfirm()))
			{
				throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다");
			}

			// 비밀번호 변경
			user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
		}

		userRepository.save(user);
	}

	/**
	 * 액세스 토큰 정보 업데이트
	 *
	 * @param user            토큰 정보를 업데이트할 유저 객체
	 * @param accessTokenInfo 업데이트할 액세스 토큰 정보
	 */
	@Transactional
	public void updateAccessToken(User user, JwtTokenInfo.AccessTokenInfo accessTokenInfo)
	{
		// 액세스 토큰 정보 업데이트
		user.updateAccessTokenInfo(accessTokenInfo);
		// 변경 사항 DB에 저장
		userRepository.save(user);
	}

	/**
	 * 리프레시 토큰 정보 업데이트 인증 / 인가에서 사용
	 * 
	 * @param user             토큰 정보를 업데이트할 유저 객체
	 * @param refreshTokenInfo 업데이트할 리프레시 토큰 정보
	 */
	@Transactional
	public void updateRefreshToken(User user, JwtTokenInfo.RefreshTokenInfo refreshTokenInfo)
	{
		// 리프레시 토큰 정보 업데이트
		user.updateRefreshTokenInfo(refreshTokenInfo);
		// 변경 사항 DB에 저장
		userRepository.save(user);
	}

	/**
	 * 액세스 토큰 기반 유저 확인
	 *
	 * @param accessToken 유저의 액세스 토큰
	 * @return 주어진 액세스 토큰을 가진 유저
	 * @throws BaseException 주어진 액세스 토큰을 가진 유저가 없을 경우
	 */
	@Transactional(readOnly = true)
	public User findUserByAccessToken(String accessToken)
	{
		// 액세스 토큰으로 유저 찾기
		return userRepository.findUserByAccessToken(accessToken)
				.orElseThrow(() -> new IllegalArgumentException("Can't find Token"));
	}

	/**
	 * 리프레시 토큰 유효 여부 확인
	 * 
	 * @param refreshToken 유효 여부를 확인할 리프레시 토큰
	 * @return 리프레시 토큰의 유효 여부(유효한 경우 true, 만료된 경우 false)
	 */
	@Transactional(readOnly = true)
	public boolean isRefreshTokenValid(String refreshToken)
	{
		// 리프레시 토큰의 만료 시간 확인
//        LocalDateTime refreshTokenExpirationTime = userRepository
//                .findRefreshTokenExpirationTimeByRefreshToken(refreshToken);
		// 리프레시 토큰의 만료 시간 확인
		Date refreshTokenExpirationDate = userRepository.findRefreshTokenExpirationTimeByRefreshToken(refreshToken);

		// Date를 LocalDateTime으로 변환
		LocalDateTime refreshTokenExpirationTime = refreshTokenExpirationDate.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		// 현재 시간과 비교하여 유효 여부 반환
		return !refreshTokenExpirationTime.isBefore(LocalDateTime.now());
	}

	/**
	 * 유저네임으로 유저를 찾습니다.
	 * 
	 * @param username
	 * @return
	 */
	public Optional<User> findUserByUsername(String username)
	{
		return this.userRepository.findByUsername(username);
	}

	public Map<String, String> updateProfileImage(UserDetailsImpl userDetails, String absoluteFilePath) throws Exception
	{
		User user = userDetails.getUser();
		Map<String, String> result = new HashMap<>();
		try
		{
			String relativePath = "";
			if(!absoluteFilePath.equals(""))
				relativePath = absoluteFilePath
				.substring(absoluteFilePath.indexOf("/uploads") + "/uploads".length());
			// user에 ProfileImage 경로 저장
			user.updateProfileImage(relativePath);
			userRepository.save(user);
			result.put("message", "프로필 이미지 수정 성공");
			result.put("file", relativePath);
			return result;
		}
		catch(Exception e)
		{
			result.put("message", "프로필 이미지 수정 실패");
			result.put("description", e.getMessage());
			return result;
		}
	}

	public Resource getImageResource(String uploadDir, UserDetailsImpl userDetails) throws Exception
	{
		User user = userDetails.getUser();
		String profileImage = user.getProfileImage();
		if(profileImage == null) profileImage = "";
		Path imagePath = Paths.get(uploadDir + profileImage);
		Resource resource = new UrlResource(imagePath.toUri());
		return resource;
	}

	public void logoutUser(HttpServletResponse response, User user)
	{
		// 쿠키에서 토큰을 삭제
		Cookie cookie = jwtTokenUtil.removeTokenCookie();
		// 빈 쿠키를 응답으로 반환하기
		response.addCookie(cookie);
		// 액세스 토큰 초기화
		user.setAccessToken("");
		// 토큰들 만료시키기
		user.setTokenExpirationTime(LocalDateTime.now());
		userRepository.save(user);
	}

	// 회원탈퇴
	public void deleteUser(HttpServletResponse response, User user) throws Exception
	{
		// 현재 userDetails에 인증된 userId로
		// userService에서 구현된 회원탈퇴 메서드를 수행한다.
		// 일반적으로 쿠키를 먼저 삭제하고, 유저 db를 삭제해야함
		// db가 먼저 삭제될시 쿠키를 삭제할수 없다.
		logoutUser(response, user);
		userRepository.deleteById(user.getId()); // 리포지토리에서 사용자 삭제
	}

}
