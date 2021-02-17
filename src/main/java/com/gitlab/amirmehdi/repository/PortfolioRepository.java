package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.Portfolio;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, AssetCompositeKey> {
    List<Portfolio> findAllByDate(LocalDate localDate);

    List<Portfolio> findAllByUserIdAndDate(long userId, LocalDate localDate);
}
