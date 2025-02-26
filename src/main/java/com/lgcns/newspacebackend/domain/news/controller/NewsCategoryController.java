package com.lgcns.newspacebackend.domain.news.controller;

import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsCategoryResponseDto;
import com.lgcns.newspacebackend.domain.news.service.NewsCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/category")
@RequiredArgsConstructor
@Tag(name = "NewsCategoryController - 뉴스 카테고리 CRUD를 위한 API")
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;

    // 카테고리 목록 조회
	@Operation(summary = "뉴스 카테고리를 조회하는 api" , description = "뉴스카테고리 서비스에서 조회 메서드를 수행합니다.")
    @GetMapping
    public ResponseEntity<List<NewsCategoryResponseDto>> getNewsCategories() {
        return ResponseEntity.ok(newsCategoryService.getNewsCategories());
    }

    // 카테고리 등록
	@Operation(summary = "뉴스 카테고리를 생성하는 api" , description = "뉴스카테고리 서비스에서 생성 메서드를 수행합니다.")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 뉴스카테고리 정보 Dto")
    @PostMapping
    public ResponseEntity<NewsCategoryResponseDto> createNewsCategory(@RequestBody NewsCategoryRequestDto requestDto) {
        return ResponseEntity.ok(newsCategoryService.createNewsCategory(requestDto));
    }

    // 카테고리 수정
	@Operation(summary = "뉴스 카테고리를 수정하는 api" , description = "뉴스카테고리 서비스에서 수정 메서드를 수행합니다.")
	@Parameter(name = "categoryId",description = "고유성을 부여하기 위해 url에 담기는 카테고리 id")
	@Parameter(name = "requestDto",description = "요청을 보낼때 담기위한 뉴스카테고리 정보 Dto")
    @PutMapping("/{categoryId}")
    public ResponseEntity<NewsCategoryResponseDto> updateNewsCategory(@PathVariable Long categoryId,
                                                                      @RequestBody NewsCategoryRequestDto requestDto) {
        return ResponseEntity.ok(newsCategoryService.updateNewsCategory(categoryId, requestDto));
    }

    // 카테고리 삭제
	@Operation(summary = "뉴스 카테고리를 삭제하는 api" , description = "뉴스카테고리 서비스에서 삭제 메서드를 수행합니다.")
	@Parameter(name = "categoryId",description = "고유성을 부여하기 위해 url에 담기는 카테고리 id")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteNewsCategory(@PathVariable Long categoryId) {
        newsCategoryService.deleteNewsCategory(categoryId);
        return ResponseEntity.ok().build();
    }

}
