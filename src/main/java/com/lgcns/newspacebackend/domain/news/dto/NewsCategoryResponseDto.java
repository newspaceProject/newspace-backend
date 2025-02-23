package com.lgcns.newspacebackend.domain.news.dto;

import com.lgcns.newspacebackend.domain.news.entity.NewsCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsCategoryResponseDto {
    private Long id;
    private String name;
    private String icon;

    public NewsCategoryResponseDto(NewsCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.icon = category.getIcon();
    }
}
