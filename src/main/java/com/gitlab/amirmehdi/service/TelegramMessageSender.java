package com.gitlab.amirmehdi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TelegramMessageSender {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String urlString = "http://51.89.109.222:8080/tg-send";

    public TelegramMessageSender(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(TelegramMessageDto dto) {
        try {
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
            restTemplate.exchange(urlString, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.error("can't request to telegram");
        }
    }
}
