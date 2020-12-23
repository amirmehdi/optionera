package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.BourseCode;

import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BourseCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BourseCodeRepository extends JpaRepository<BourseCode, Long> {
    List<BourseCode> findAllByBrokerIn(List<Broker>brokers);
}
