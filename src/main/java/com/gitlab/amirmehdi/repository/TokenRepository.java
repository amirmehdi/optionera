package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Token;

import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Token entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTopByBrokerOrderByIdDesc(Broker broker);

    List<Token> findAllByBroker(Broker broker);

    @CacheEvict(value = "tokenByBroker")
    @Override
    <S extends Token> S save(S s);
}
