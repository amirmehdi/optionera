package com.gitlab.amirmehdi.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TelegramMessageDto {
    private String token;
    private String chatId;
    private String text;

    public TelegramMessageDto(String chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }
}
