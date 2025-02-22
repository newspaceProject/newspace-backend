package com.lgcns.newspacebackend.domain.news.controller;

import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordResponseDto;
import com.lgcns.newspacebackend.domain.news.service.NewsKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/keyword")
@RequiredArgsConstructor
public class NewsKeywordController {

    private final NewsKeywordService newsKeywordService;

    // 키워드 목록 조회
    @GetMapping
    public ResponseEntity<List<NewsKeywordResponseDto>> getNewsKeywords() {
        return ResponseEntity.ok(newsKeywordService.getNewsKeywords());
    }

    // 키워드 등록
    @PostMapping
    public ResponseEntity<NewsKeywordResponseDto> createNewsKeyword(@RequestBody NewsKeywordRequestDto requestDto) {
        return ResponseEntity.ok(newsKeywordService.createNewsKeyword(requestDto));
    }

    // 키워드 수정
    @PutMapping("/{keywordId}")
    public ResponseEntity<NewsKeywordResponseDto> updateNewsKeyword(@PathVariable Long keywordId,
                                                                    @RequestBody NewsKeywordRequestDto requestDto) {
        return ResponseEntity.ok(newsKeywordService.updateNewsKeyword(keywordId, requestDto));
    }

    // 키워드 삭제
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<NewsKeywordResponseDto> deleteNewsKeyword(@PathVariable Long keywordId) {
        newsKeywordService.deleteNewsKeyword(keywordId);
        return ResponseEntity.ok().build();
    }
}
