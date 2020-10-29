package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.InstrumentHistoryCompositeKey;
import com.gitlab.amirmehdi.domain.InstrumentTradeHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InstrumentHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentTradeHistoryRepository extends JpaRepository<InstrumentTradeHistory, InstrumentHistoryCompositeKey> {

    List<InstrumentTradeHistory> findAllByIsin(String isin, Pageable pageable);
}
