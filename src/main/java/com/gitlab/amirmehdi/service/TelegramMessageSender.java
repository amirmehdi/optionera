package com.gitlab.amirmehdi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramMessageSender {
    private final RestTemplate restTemplate;
    private final String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private final String apiToken = "1154072624:AAG1HWOZDAU4FxgP0aek84zt7Ap4mfe4wJo";

    public TelegramMessageSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String chatId, String text) {
        String url = String.format(urlString, apiToken, chatId, text);
        restTemplate.getForEntity(url,String.class);
    }
}
