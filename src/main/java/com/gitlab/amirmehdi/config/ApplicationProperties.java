package com.gitlab.amirmehdi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to E Trade.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {
    private String oaBaseUrl;

    private final Schedule schedule = new Schedule();
    private final Telegram telegram = new Telegram();
    private final Strategy strategy = new Strategy();

    @Getter
    @Setter
    public static class Schedule{
        private boolean timeCheck;
        private String arbitrage;
        private String market;
        private String interest;
    }

    @Getter
    @Setter
    public static class Telegram{
        private String token;
        private String publicChat;
        private String privateChat;
    }

    @Getter
    @Setter
    public static class Strategy{
        private float unusual1Threshold;
    }
}
