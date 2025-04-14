package com.lgcns.newspacebackend.domain.news.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.newspacebackend.domain.news.dto.NewsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class NewsAIService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public NewsAIService(ChatClient chatClient, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    @Async
    public List<NewsResponseDto> getPastNews(String keyword) {
        try {
            String formattedDate = LocalDate.now().toString().substring(5); // "MM-DD" 형식
            String prompt = buildPrompt(formattedDate, keyword);
            String response = chatClient.prompt().user(prompt).call().content();

            log.info("AI 응답: {}", response);

            String jsonResponse = extractJsonFromResponse(response);
            if (jsonResponse != null) {
                return objectMapper.readValue(jsonResponse, new TypeReference<List<NewsResponseDto>>() {});
            } else {
                log.warn("AI 응답에서 JSON 부분을 찾을 수 없음.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("뉴스 데이터 파싱 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildPrompt(String formattedDate, String keyword) {
        return String.format(
                "Provide a list of 10 famous news articles from previous years that happened on '%s' worldwide, " +
                        "focusing on the topic '%s'. The list should be sorted by popularity, not by date. " +
                        "Each article must include an incrementing 'id' field starting from 1, " +
                        "title, summary, date (YYYY-MM-DD format), source, and a valid URL link to the full article. " +
                        "Ensure the response is a strict JSON array in the following format: " +
                        "[{\"id\":1, \"title\":\"News title\",\"content\":\"News summary\",\"date\":\"YYYY-MM-DD\",\"source\":\"News source\",\"link\":\"URL\"}]. " +
                        "Translate all content into Korean.",
                formattedDate, keyword
        );
    }

    private String extractJsonFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\[.*\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? matcher.group(0) : null;
    }
}
