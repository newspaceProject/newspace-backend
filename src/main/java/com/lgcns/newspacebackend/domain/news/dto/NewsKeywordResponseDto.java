package com.lgcns.newspacebackend.domain.news.dto;

import com.lgcns.newspacebackend.domain.news.entity.NewsKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsKeywordResponseDto {
    private Long id;
    private String name;

    public NewsKeywordResponseDto(NewsKeyword keyword) {
        this.id = keyword.getId();
        this.name = keyword.getName();
    }
}
