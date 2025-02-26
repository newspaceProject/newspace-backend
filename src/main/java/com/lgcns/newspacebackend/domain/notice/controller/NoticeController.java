package com.lgcns.newspacebackend.domain.notice.controller;

import com.lgcns.newspacebackend.domain.notice.dto.NoticeRequestDto;
import com.lgcns.newspacebackend.domain.notice.dto.NoticeResponseDto;
import com.lgcns.newspacebackend.domain.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Tag(name = "NoticeController - 관리자 공지사항 CRUD를 위한 API")
public class NoticeController {
    private final NoticeService noticeService;

    // 메인화면 공지 조회
	@Operation(summary = "공지사항을 불러오는 api" , description = "공지사항 서비스의 조회 메서드를 수행합니다.")
    @GetMapping
    public ResponseEntity<NoticeResponseDto> getMainNotice() {
        return ResponseEntity.ok(noticeService.getMainNotice());
    }

    // 공지 등록
	@Operation(summary = "공지사항을 등록하는 api" , description = "공지사항 서비스의 생성 메서드를 수행합니다.")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 공지사항 Dto")
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.createNotice(requestDto));
    }

    // 공지 수정
	@Operation(summary = "공지사항을 수정하는 api" , description = "공지사항 서비스의 수정 메서드를 수행합니다.")
	@Parameter(name = "noticeId",description = "고유성을 부여하기 위해 url에 담기는 공지사항 id")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 뉴스키워드 정보 Dto")
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId,
                                                          @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, requestDto));
    }

    // 공지 삭제
	@Operation(summary = "공지사항을 삭제하는 api" , description = "공지사항 서비스의 삭제 메서드를 수행합니다.")
	@Parameter(name = "noticeId",description = "고유성을 부여하기 위해 url에 담기는 공지사항 id")
    @DeleteMapping("/noticeId")
    public ResponseEntity<NoticeResponseDto> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
