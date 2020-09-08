package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.InstrumentHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InstrumentHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentHistoryRepository extends JpaRepository<InstrumentHistory, Long> {
}
