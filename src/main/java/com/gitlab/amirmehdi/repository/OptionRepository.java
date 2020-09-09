package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.domain.Option;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data  repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByInstrumentIsinAndStrikePriceAndExpDate(String isin, Integer strikePrice, LocalDate expDate);
}
