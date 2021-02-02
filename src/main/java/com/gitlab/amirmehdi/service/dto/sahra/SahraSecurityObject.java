package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SahraSecurityObject {
    private String connectionToken;
    private String messageId;
    private String groupToken;

    public SahraSecurityObject(String fields) {
        String[] strings = fields.split("__");
        this.connectionToken = strings[0];
        this.messageId = strings[1];
        this.groupToken = strings[2];
    }
}
