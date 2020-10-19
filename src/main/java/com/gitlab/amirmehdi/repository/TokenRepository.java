package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Token entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTopByBrokerOrderByCreatedAtDesc(Broker broker);

    Optional<Token> findTopByBrokerOrderByIdDesc(Broker broker);
}
