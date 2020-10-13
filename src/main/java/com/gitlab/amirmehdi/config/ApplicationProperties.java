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
    private int oaRateLimit;
    private String apiToken;
    private String channelId;
    private String privateChannelId;

//    private boolean marketTimeCheck;
//    private String marketUpdaterFixedRate;
//    private String openInterestUpdaterFixedRate;
}
