package com.lgcns.newspacebackend.domain.notice.controller;

import com.lgcns.newspacebackend.domain.notice.dto.NoticeRequestDto;
import com.lgcns.newspacebackend.domain.notice.dto.NoticeResponseDto;
import com.lgcns.newspacebackend.domain.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Tag(name = "NoticeController - 관리자 공지사항 관련 crud api")
public class NoticeController {
    private final NoticeService noticeService;

    // 메인화면 공지 조회
    @GetMapping
    public ResponseEntity<NoticeResponseDto> getMainNotice() {
        return ResponseEntity.ok(noticeService.getMainNotice());
    }

    // 공지 등록
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.createNotice(requestDto));
    }

    // 공지 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId,
                                                          @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, requestDto));
    }

    // 공지 삭제
    @DeleteMapping("/noticeId")
    public ResponseEntity<NoticeResponseDto> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
