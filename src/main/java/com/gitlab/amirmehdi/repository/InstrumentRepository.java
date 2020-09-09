package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Instrument;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Instrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    Instrument findOneByName(String name);
}
