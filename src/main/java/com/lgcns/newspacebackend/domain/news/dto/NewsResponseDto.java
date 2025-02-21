package com.lgcns.newspacebackend.domain.news.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsResponseDto {
    private String title;
    private String content;
    private String date;
    private String source; // newsCompany
    private String link;
}
