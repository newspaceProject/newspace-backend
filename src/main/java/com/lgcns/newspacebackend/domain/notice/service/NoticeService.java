package com.lgcns.newspacebackend.domain.notice.service;

import com.lgcns.newspacebackend.domain.notice.dto.NoticeRequestDto;
import com.lgcns.newspacebackend.domain.notice.dto.NoticeResponseDto;
import com.lgcns.newspacebackend.domain.notice.entity.Notice;
import com.lgcns.newspacebackend.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 메인 공지사항 등록
    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {

        Notice notice = Notice.builder()
                .content(requestDto.getContent())
                .isMain(true)
                .build();

        noticeRepository.save(notice);

        return new NoticeResponseDto(notice);
    }

    // 메인 공지 조회
    @Transactional(readOnly = true)
    public NoticeResponseDto getMainNotice() {

        Notice notice = noticeRepository.findByIsMainTrue().orElse(null);
        return (notice != null) ? new NoticeResponseDto(notice) : null;
    }

    // 공지사항 수정
    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
        Notice notice = getNoticeForRepository(noticeId);

        notice.setContent(requestDto.getContent());
        noticeRepository.save(notice);

        return new NoticeResponseDto(notice);
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = getNoticeForRepository(noticeId);

        noticeRepository.delete(notice);
    }

    // noticeId로 repository에서 notice 조회
    private Notice getNoticeForRepository(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("해당 공지사항 id에 맞는 공지사항을 찾을 수 없습니다."));
        return notice;
    }
}
