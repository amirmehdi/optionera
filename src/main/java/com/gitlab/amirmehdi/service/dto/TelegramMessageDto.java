package com.gitlab.amirmehdi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TelegramMessageDto {
    private String token;
    private String chatId;
    private String text;
}
