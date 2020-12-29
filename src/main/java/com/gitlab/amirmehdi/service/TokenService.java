package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.repository.TokenRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Cacheable(value = "tokenByBroker")
    public List<Token> getAvailableTokens(Broker broker) {
        return tokenRepository.findAllByBroker(broker);
    }
}
