package com.lgcns.newspacebackend.domain.news.controller;

import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryResponseDto;
import com.lgcns.newspacebackend.domain.news.service.NewsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/category")
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;

    // 카테고리 목록 조회
    @GetMapping
    public ResponseEntity<List<NewsCategoryResponseDto>> getNewsCategories() {
        return ResponseEntity.ok(newsCategoryService.getNewsCategories());
    }

    // 카테고리 등록
    @PostMapping
    public ResponseEntity<NewsCategoryResponseDto> createNewsCategory(@RequestBody NewsCategoryRequestDto requestDto) {
        return ResponseEntity.ok(newsCategoryService.createNewsCategory(requestDto));
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<NewsCategoryResponseDto> updateNewsCategory(@PathVariable Long categoryId,
                                                                      @RequestBody NewsCategoryRequestDto requestDto) {
        return ResponseEntity.ok(newsCategoryService.updateNewsCategory(categoryId, requestDto));
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteNewsCategory(@PathVariable Long categoryId) {
        newsCategoryService.deleteNewsCategory(categoryId);
        return ResponseEntity.ok().build();
    }

}
