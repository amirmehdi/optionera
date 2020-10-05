package com.gitlab.amirmehdi.service.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessageDto {
    private String token;
    private String chatId;
    private String text;

    public String getToken() {
        return token;
    }

    public TelegramMessageDto setToken(String token) {
        this.token = token;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public TelegramMessageDto setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getText() {
        return text;
    }

    public TelegramMessageDto setText(String text) {
        this.text = text;
        return this;
    }
}
