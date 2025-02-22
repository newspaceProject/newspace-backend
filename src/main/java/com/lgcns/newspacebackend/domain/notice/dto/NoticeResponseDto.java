package com.lgcns.newspacebackend.domain.notice.dto;

import com.lgcns.newspacebackend.domain.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String content;

    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.content = notice.getContent();
    }
}
