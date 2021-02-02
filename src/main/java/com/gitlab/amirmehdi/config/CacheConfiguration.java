package com.gitlab.amirmehdi.config;

import com.gitlab.amirmehdi.domain.InstrumentTradeHistory;
import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.gitlab.amirmehdi.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.gitlab.amirmehdi.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.gitlab.amirmehdi.domain.User.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Authority.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.User.class.getName() + ".authorities");
            createCache(cm, com.gitlab.amirmehdi.domain.Instrument.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Instrument.class.getName() + ".options");
            createCache(cm, com.gitlab.amirmehdi.domain.Option.class.getName());
            createCache(cm, InstrumentTradeHistory.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Signal.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Signal.class.getName() + ".orders");
            createCache(cm, com.gitlab.amirmehdi.domain.Order.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Token.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Board.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.Portfolio.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.OpenInterest.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.BourseCode.class.getName());
//            createCache(cm, "tokenByBroker");
            createCache(cm, com.gitlab.amirmehdi.domain.EmbeddedOption.class.getName());
            createCache(cm, com.gitlab.amirmehdi.domain.BourseCode.class.getName() + ".tokens");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("tokenByBroker");
//    }

}
