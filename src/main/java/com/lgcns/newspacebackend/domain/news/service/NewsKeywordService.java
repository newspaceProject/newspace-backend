package com.lgcns.newspacebackend.domain.news.service;

import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordRequestDto;
import com.lgcns.newspacebackend.domain.news.dto.NewsKeywordResponseDto;
import com.lgcns.newspacebackend.domain.news.entity.NewsKeyword;
import com.lgcns.newspacebackend.domain.news.repository.NewsKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsKeywordService {
    private final NewsKeywordRepository newsKeywordRepository;

    // 카테고리 등록
    @Transactional
    public NewsKeywordResponseDto createNewsKeyword(NewsKeywordRequestDto requestDto) {
        NewsKeyword keyword = NewsKeyword.builder()
                .name(requestDto.getName())
                .build();
        newsKeywordRepository.save(keyword);

        return new NewsKeywordResponseDto(keyword);
    }

    // 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<NewsKeywordResponseDto> getNewsKeywords() {
        List<NewsKeywordResponseDto> keywordResponseDtoList = newsKeywordRepository.findAll()
                .stream()
                .map(NewsKeywordResponseDto::new)
                .collect(Collectors.toList());

        return keywordResponseDtoList;
    }

    // 카테고리 수정
    @Transactional
    public NewsKeywordResponseDto updateNewsKeyword(Long keywordId, NewsKeywordRequestDto requestDto) {
        NewsKeyword keyword = getNewsKeywordForRepository(keywordId);

        keyword.setName(requestDto.getName());
        newsKeywordRepository.save(keyword);

        return new NewsKeywordResponseDto(keyword);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteNewsKeyword(Long keywordId) {
        NewsKeyword keyword = getNewsKeywordForRepository(keywordId);

        newsKeywordRepository.delete(keyword);
    }

    // keywordId로 repository 에서 keyword 조회
    private NewsKeyword getNewsKeywordForRepository(Long keywordId) {
        NewsKeyword keyword = newsKeywordRepository.findById(keywordId)
                .orElseThrow(() -> new RuntimeException("해당 카테고리 id에 맞는 카테고리를 찾을 수 없습니다."));
        return keyword;
    }
}
