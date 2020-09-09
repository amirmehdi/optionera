package com.gitlab.amirmehdi.config;

import com.gitlab.amirmehdi.domain.OptionStats;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, OptionStats> optionStatsRedisTemplate() {
        return new RedisTemplate<>();
    }
}
