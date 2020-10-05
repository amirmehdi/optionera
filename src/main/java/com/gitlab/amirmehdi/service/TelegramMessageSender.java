package com.gitlab.amirmehdi.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TelegramMessageSender {
    private final RestTemplate restTemplate;
    private final String urlString = "http://51.89.109.222:8080/tg-send?token=%s&chatId=%s&text=%s";
    private final String apiToken = "1154072624:AAG1HWOZDAU4FxgP0aek84zt7Ap4mfe4wJo";

    public TelegramMessageSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String chatId, String text) {
        String url = String.format(urlString, apiToken, chatId, text);
        try {
            restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            log.error("can't request to telegram");
        }
    }
}
