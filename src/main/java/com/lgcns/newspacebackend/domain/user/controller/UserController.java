package com.lgcns.newspacebackend.domain.user.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.newspacebackend.domain.notice.service.NoticeService;
import com.lgcns.newspacebackend.domain.user.entity.User;
import com.lgcns.newspacebackend.global.security.UserDetailsImpl;
import com.lgcns.newspacebackend.global.util.FileUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
	@Autowired
	private FileUtil fileUtil;
	
	//프로필 사진 등록
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<Object> createProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails, MultipartHttpServletRequest request) throws Exception
	{
        String username = userDetails.getName();
        User user = this.service.getUser(username);
        String imagePath = fileUtil.getFilePath(request);

        Map<String, String> result = new HashMap<>();
        try {
        	//TODO 서비스에서 user 에 imagePath 저장
            result.put("message", "프로필 이미지 저장 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch(Exception e) {
            result.put("message", "프로필 이미지 저장 실패");
            result.put("description", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);            
        }
	}
    
    //프로필 사진 수정
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<Object> updateProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails, MultipartHttpServletRequest request) throws Exception
	{
    	String username = userDetails.getName();
        User user = this.service.getUser(username);
        String imagePath = fileUtil.getFilePath(request);
        Map<String, String> result = new HashMap<>();
        try {
        	//TODO 서비스에서 user 에 imagePath 저장
            result.put("message", "프로필 이미지 수정 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch(Exception e) {
            result.put("message", "프로필 이미지 수정 실패");
            result.put("description", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);            
        }
	}
    
    //프로필 사진 삭제
    @DeleteMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity<Object> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails)
	{
    	String username = userDetails.getName();
        User user = this.service.getUser(username);
        Map<String, String> result = new HashMap<>();
        try {
        	//TODO 서비스에서 user 에 imagePath 를 기본 이미지로 변경 해야함
            result.put("message", "프로필 이미지 삭제 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch(Exception e) {
            result.put("message", "프로필 이미지 삭제 실패");
            result.put("description", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);            
        }
	}
	
    //프로필 사진 다운로드
    @GetMapping("/profile")
    public ResponseEntity<Resource> getProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) throws MalformedURLException {

    	String username = userDetails.getName();
        User user = this.service.getUser(username);
		String profileImage = user.getProfileImage();
        Path imagePath = Paths.get(profileImage);
        
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // 이미지 형식에 맞게 수정
                    //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"profile.jpg\"") // 다운로드의 경우
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //이미지 탐색 kudong.kr:55021/api/user/image/0/default_profile.jpg
    //이미지 예시 
    @GetMapping("/image/{day}/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("day") String day, @PathVariable("filename") String filename) throws MalformedURLException {
        Path imagePath = Paths.get(uploadPath+ day).resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // 이미지 형식에 맞게 수정
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
