package com.lgcns.newspacebackend.domain.news.controller;

import com.lgcns.newspacebackend.domain.news.dto.NewsResponseDto;
import com.lgcns.newspacebackend.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> getNews(@RequestParam String category) {
        log.info("[GET] /api/news 요청 - category: {}", category); // 요청 로그

        try {
            List<NewsResponseDto> newsList = newsService.getPastNews(category);

            if (newsList.isEmpty()) {
                log.warn("뉴스 데이터 없음 - category: {}", category);
                return ResponseEntity.status(500).build(); // 실패 시 500 에러 반환
            }

            log.info("뉴스 데이터 응답 성공 - category: {}, 개수: {}", category, newsList.size());
            return ResponseEntity.ok(newsList);

        } catch (Exception e) {
            log.error("뉴스 데이터 조회 중 예외 발생 - category: {}", category, e);
            return ResponseEntity.status(500).body(null);
        }
    }
}