package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Instrument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Instrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    Optional<Instrument> findOneByName(String name);

    List<Instrument> findAllByNameLike(String name, Pageable pageable);

    @Query(nativeQuery = true
    ,value = "select i " +
        "from instrument i where i.isin not in (select distinct(instrument_id) from option)")
    List<Instrument> findAllInstrumentHasNotOption();
}
