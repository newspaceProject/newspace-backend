package com.lgcns.newspacebackend.domain.user.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lgcns.newspacebackend.domain.user.dto.SignupRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoRequestDto;
import com.lgcns.newspacebackend.domain.user.dto.UserInfoResponseDto;
import com.lgcns.newspacebackend.domain.user.service.UserService;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
import com.lgcns.newspacebackend.global.util.FileUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController
{

	private final UserService userService;

	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;

	@Autowired
	private FileUtil fileUtil;

	// 로그아웃 기능 -> 쿠키에서 토큰 삭제하기
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletResponse response,
			@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception
	{
		userService.logoutUser(response, userDetails.getUser());
		return ResponseEntity.ok("로그아웃 완료");
	}

	// 회원탈퇴 기능
	@DeleteMapping("/signout")
	public ResponseEntity<String> deleteUser(HttpServletResponse response,
			@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception
	{
		userService.deleteUser(response, userDetails.getUser());
		return ResponseEntity.ok("유저 삭제 완료");
	}

	// 프로필 사진 등록
	@PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<Object> createProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
			MultipartHttpServletRequest request) throws Exception
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.userService.updateProfileImage(userDetails, fileUtil.getAbsoluteFilePath(request)));
	}

	// 프로필 사진 수정
	@PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<Object> updateProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
			MultipartHttpServletRequest request) throws Exception
	{
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.userService.updateProfileImage(userDetails, fileUtil.getAbsoluteFilePath(request)));
	}

	// 프로필 사진 삭제
	@DeleteMapping("/profile")
	private ResponseEntity<Object> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails)
			throws Exception
	{
		return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateProfileImage(userDetails, ""));
	}

	// 프로필 사진 다운로드
	@GetMapping("/profile")
	public ResponseEntity<Resource> getProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails)
			throws Exception
	{
		Resource resource = this.userService.getImageResource(this.uploadPath,userDetails);
		log.info("이미지 파일 => "+resource.getFilename());
		if(resource.exists() || resource.isReadable())
		{
			String imageType = this.fileUtil.getFileExtension(resource.getFilename());
			String fileName = "profile.jpg";
			MediaType mediaType = MediaType.IMAGE_JPEG;

			if("png".equalsIgnoreCase(imageType))
			{
				mediaType = MediaType.IMAGE_PNG;
				fileName = "profile.png";
			}

			return ResponseEntity.ok().contentType(mediaType) // 이미지 형식에 맞게 수정
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"").body(resource);
		}
		else return ResponseEntity.notFound().build();
	}

	// 경로로 이미지 검색
	@GetMapping("/image/{day}/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable("day") String day, @PathVariable("filename") String filename)
			throws MalformedURLException
	{
		Path imagePath = Paths.get(uploadPath+"/"+ day).resolve(filename);
		Resource resource = new UrlResource(imagePath.toUri());
		
		if(resource.exists() || resource.isReadable())
		{
			String imageType = this.fileUtil.getFileExtension(imagePath.toUri().toString());
			String fileName = "profile.jpg";
			MediaType mediaType = MediaType.IMAGE_JPEG;

			if("png".equalsIgnoreCase(imageType))
			{
				mediaType = MediaType.IMAGE_PNG;
				fileName = "profile.png";
			}

			return ResponseEntity.ok().contentType(mediaType) // 이미지 형식에 맞게 수정
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"").body(resource);
		}
		else return ResponseEntity.notFound().build();
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult)
			throws MethodArgumentNotValidException
	{
		userService.signup(requestDto, bindingResult);
		return ResponseEntity.ok("회원가입 성공");
	}

	// 아이디 중복 체크
	@GetMapping("/check-id")
	public ResponseEntity<?> checkId(@RequestParam("username") String username)
	{
		boolean isValid = userService.checkId(username);

		if(isValid)
		{
			return ResponseEntity.ok().build();
		}
		else
		{
			return ResponseEntity.badRequest().build();
		}
	}

	// 회원정보 조회
	@GetMapping("/info")
	public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails)
	{
		return ResponseEntity.ok(userService.getUserInfo(userDetails.getUser().getId()));
	}

	// 회원정보 수정
	@PatchMapping("/info")
	public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody UserInfoRequestDto requestDto)
	{
		userService.updateUserInfo(userDetails.getUser().getId(), requestDto);
		return ResponseEntity.ok().build();
	}
}
