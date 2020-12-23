package com.gitlab.amirmehdi.service.dto.sahra;

import com.gitlab.amirmehdi.domain.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
public class SecurityFields {
    private String connectionToken;
    private String messageId;
    private String groupToken;
    private Token token;
    private List<ScheduledFuture<?>> schedules= new ArrayList<>();
    private int i = 0;

    public void clear() {
        connectionToken = null;
        messageId = null;
        groupToken = null;
        token = null;
        schedules.forEach(scheduledFuture -> scheduledFuture.cancel(true));
        schedules.clear();
    }

    public long incAndGet() {
        return ++i;
    }
}
