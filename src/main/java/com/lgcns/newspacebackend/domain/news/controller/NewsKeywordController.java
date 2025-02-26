package com.lgcns.newspacebackend.domain.news.controller;

import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordResponseDto;
import com.lgcns.newspacebackend.domain.news.service.NewsKeywordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/keyword")
@RequiredArgsConstructor
@Tag(name = "NewsKeywordController - 뉴스 키워드 crud를 위한 api")
public class NewsKeywordController {

    private final NewsKeywordService newsKeywordService;

    // 키워드 목록 조회
	@Operation(summary = "뉴스 키워드를 조회하는 api" , description = "뉴스키워드 서비스에서 조회 메서드를 수행합니다.")
	@GetMapping
    public ResponseEntity<List<NewsKeywordResponseDto>> getNewsKeywords() {
        return ResponseEntity.ok(newsKeywordService.getNewsKeywords());
    }

	
    // 키워드 등록
	@Operation(summary = "뉴스 키워드를 생성하는 api" , description = "뉴스키워드 서비스에서 생성 메서드를 수행합니다.")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 뉴스키워드 정보 Dto")
    @PostMapping
    public ResponseEntity<NewsKeywordResponseDto> createNewsKeyword(@RequestBody NewsKeywordRequestDto requestDto) {
        return ResponseEntity.ok(newsKeywordService.createNewsKeyword(requestDto));
    }

    // 키워드 수정
	@Operation(summary = "뉴스 키워드를 수정하는 api" , description = "뉴스키워드 서비스에서 수정 메서드를 수행합니다.")
	@Parameter(name = "keywordId",description = "고유성을 부여하기 위해 url에 담기는 키워드 id")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 뉴스키워드 정보 Dto")
    @PutMapping("/{keywordId}")
    public ResponseEntity<NewsKeywordResponseDto> updateNewsKeyword(@PathVariable Long keywordId,
                                                                    @RequestBody NewsKeywordRequestDto requestDto) {
        return ResponseEntity.ok(newsKeywordService.updateNewsKeyword(keywordId, requestDto));
    }

    // 키워드 삭제
	@Operation(summary = "뉴스 키워드를 삭제하는 api" , description = "뉴스키워드 서비스에서 삭제 메서드를 수행합니다.")
	@Parameter(name = "keywordId",description = "고유성을 부여하기 위해 url에 담기는 키워드 id")
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<NewsKeywordResponseDto> deleteNewsKeyword(@PathVariable Long keywordId) {
        newsKeywordService.deleteNewsKeyword(keywordId);
        return ResponseEntity.ok().build();
    }
}
