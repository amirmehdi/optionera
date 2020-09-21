package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Instrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    Optional<Instrument> findOneByName(String name);
}
