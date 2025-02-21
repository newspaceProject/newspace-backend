package com.lgcns.newspacebackend.domain.news.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.newspacebackend.domain.news.dto.NewsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class NewsService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public List<NewsResponseDto> getPastNews(String category) {
        try {
            // 오늘 날짜 가져오기
            String today = LocalDate.now().toString();
            String formattedDate = today.substring(5);  // "MM-DD" 형식

            // 프롬프트 생성 및 AI 호출
            String response = chatClient.prompt()
                    .user(String.format(
                            "전세계 기준으로 과거 년도의 '%s' 날짜에 있었던 '%s' 주제의 뉴스 기사를 날짜 순이 아닌 유명한 순서대로 10개를 JSON 형식으로 응답해줘. " +
                                    "link는 본문 기사 링크로 연결될 수 있게 해주고, 반드시 정확한 JSON 배열로 반환해야 하고 각 내용은 한글로 번역해서 넣어줘, 예제: " +
                                    "[{\"title\":\"뉴스 제목\",\"content\":\"뉴스 내용\",\"date\":\"YYYY-MM-DD\",\"source\":\"뉴스사\",\"link\":\"URL\"}]",
                            formattedDate, category
                    ))
                    .call()
                    .content();

            log.info("AI 응답 : {}", response);

            // JSON 부분만 추출 (정규식 사용)
            Pattern pattern = Pattern.compile("\\[.*\\]", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                String jsonResponse = matcher.group(0); // JSON 배열 부분만 추출
                log.info("추출된 JSON: {}", jsonResponse);
                return objectMapper.readValue(jsonResponse, new TypeReference<List<NewsResponseDto>>() {});
            } else {
                log.warn("⚠ AI 응답에서 JSON 부분을 찾을 수 없음.");
                return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("뉴스 데이터 파싱 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
