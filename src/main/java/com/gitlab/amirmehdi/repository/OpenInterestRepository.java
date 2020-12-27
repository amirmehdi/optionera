package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.OpenInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the OpenInterest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpenInterestRepository extends JpaRepository<OpenInterest, AssetCompositeKey> {
    List<OpenInterest> findAllByDate(LocalDate now);
}
