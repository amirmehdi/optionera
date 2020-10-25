package com.gitlab.amirmehdi.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MetricRegistryConfiguration {

    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        registry
            .config()
            .meterFilter(MeterFilter.maxExpected("omid.rlc", Duration.ofSeconds(1)))
            .meterFilter(MeterFilter.minExpected("omid.rlc", Duration.ofMillis(100)));
        return new TimedAspect(registry);
    }
}
