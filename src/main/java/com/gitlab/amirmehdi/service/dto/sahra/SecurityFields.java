package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityFields {
    private String connectionToken;
    private String messageId;
    private String groupToken;
    private String token;
    private int i = 0;

    public void clear() {
        connectionToken = null;
        messageId = null;
        groupToken = null;
        token = null;
    }

    public long incAndGet() {
        return ++i;
    }
}
