package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.domain.Signal;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResponse {
    private List<Signal> callSignals;
    private String publicChatId;
    // only for private channel
    private SendOrderType sendOrderType;
    private String privateChatId;

    public enum SendOrderType {
        NONE, SEND_NOW, NEED_ALLOW
    }
}
