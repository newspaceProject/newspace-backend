package com.lgcns.newspacebackend.domain.notice.dto;

import com.lgcns.newspacebackend.domain.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {
    private String content;

    public NoticeResponseDto(Notice notice) {
        this.content = notice.getContent();
    }
}
