package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Signal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignalRepository extends JpaRepository<Signal, Long>, JpaSpecificationExecutor<Signal> {
}
