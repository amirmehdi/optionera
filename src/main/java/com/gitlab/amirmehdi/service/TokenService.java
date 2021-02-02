package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OMS;
import com.gitlab.amirmehdi.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService implements CommandLineRunner {
    private final TokenRepository tokenRepository;
    private final HashMap<OMS, TokenUpdater> tokenUpdaterHashMap = new HashMap<>();

    @Autowired
    public void setTokenUpdaters(List<TokenUpdater> tokenUpdaters) {
        for (TokenUpdater tokenUpdater : tokenUpdaters) {
            tokenUpdaterHashMap.put(tokenUpdater.getOMS(), tokenUpdater);
        }
    }

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> getAvailableTokens(Broker broker) {
        return tokenRepository.findAllByBourseCode_Broker(broker);
    }

    @Override
    public void run(String... args) throws Exception {
        updateAllTokens();
    }

    @Scheduled(fixedRate = 6 * 3600 * 1000)
    public void removingOldToken() {
        updateAllTokens();
    }

    @Scheduled(cron = "0 28 8 * * *")
    public void updateAllTokens() {
        List<Token> tokens = tokenRepository.findAll().stream()
            .filter(token -> token.getCreatedAt().isBefore(Instant.now().minus(6, ChronoUnit.HOURS)))
            .collect(Collectors.toList());
        tokenRepository.deleteAll(tokens);
        for (TokenUpdater value : tokenUpdaterHashMap.values()) {
            value.updateAllTokens();
        }
    }

    public HashMap<OMS, TokenUpdater> getTokenUpdaters() {
        return tokenUpdaterHashMap;
    }
}
