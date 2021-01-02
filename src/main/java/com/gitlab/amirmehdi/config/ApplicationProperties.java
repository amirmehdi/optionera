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
    private String seleniumHubGrid;

    private final Brokers brokers = new Brokers();
    private final Crawler crawler = new Crawler();
    private final Schedule schedule = new Schedule();
    private final Telegram telegram = new Telegram();
    private final Strategy strategy = new Strategy();
    private final Headline headline = new Headline();

    @Getter
    @Setter
    public static class Brokers {
        private boolean sahraEnable;
    }

    @Getter
    @Setter
    public static class Crawler {
        private String bestCrawler;
        private String availableCrawler; //OmidCrawler:0:150,AsaCrawler:150:225,TadbirCrawler:225:400
        private int tadbirChunk;
        private int asaChunk;
        private int omidChunk;
    }


    @Getter
    @Setter
    public static class Schedule {
        private boolean timeCheck;
        private String arbitrage;
        private String market;
        private String interest;
        private String clientsInfo;
        private String optionTable;
    }

    @Getter
    @Setter
    public static class Telegram {
        private String token;
        private String publicChat;
        private String privateChat;
        private String healthCheckChat;
    }

    @Getter
    @Setter
    public static class Strategy {
        private float unusual1Threshold;
    }

    @Getter
    @Setter
    public static class Headline {
        private String cron;
        private int repeat;
        private long sleep;
    }
}
