package com.jkk9494.dev.k_grammarChecker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkk9494.dev.k_grammarChecker.dto.GrammarRequest;
import com.jkk9494.dev.k_grammarChecker.dto.GrammarResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GrammarService {
    private final String apiKey;

    public GrammarService(@Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public GrammarResponse checkGrammar(GrammarRequest request) {
        String endpoint = "https://api.openai.com/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", List.of(Map.of(
                "role", "user",
                "content", request.getText() + "\n\n이 텍스트의 맞춤법과 문법을 검사하고 제안하세요. 결과텍스트만 보여주세요."
        )));
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.5);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            // 응답에서 텍스트 가져오기
            List<?> choices = (List<?>) responseMap.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = (Map<String, Object>) choices.get(0);
                Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                if (message != null) {
                    String correctedText = message.get("content");
                    if (correctedText != null) {
                        GrammarResponse grammarResponse = new GrammarResponse();
                        grammarResponse.setCorrectedText(correctedText.trim());
                        return grammarResponse;
                    }
                }
            }

            throw new RuntimeException("No content found in the response");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse response from OpenAI API");
        }
    }
}
